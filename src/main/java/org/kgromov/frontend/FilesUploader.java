package org.kgromov.frontend;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.FileData;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.kgromov.assistant.DocumentType;
import org.kgromov.assistant.EmbeddingsService;
import org.springframework.util.ReflectionUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@SpringComponent
@Slf4j
@UIScope
@RequiredArgsConstructor
public class FilesUploader extends VerticalLayout {
    private final EmbeddingsService embeddingsService;
    private final MultiFileMemoryBuffer memoryBuffer = new MultiFileMemoryBuffer();
    private final Upload fileUploader = new Upload(memoryBuffer);
    private final static Map<String, FileData> FILES_CACHE = new ConcurrentHashMap<>();

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        this.configureFileUploader();
        this.customizedUploaderLayout();
//        FILES_CACHE.forEach((fileName, fileDate) -> memoryBuffer.receiveUpload(fileName, fileDate.getMimeType()));
        this.copyValuesFromCache();
    }

    private void configureFileUploader() {
        fileUploader.setMaxFileSize(10 * 1024 * 1024);
        fileUploader.setAcceptedFileTypes(
                "application/pdf", ".pdf",
                // Microsoft Excel (.xls)
                "application/vnd.ms-excel", ".xls",
                // Microsoft Excel (OpenXML, .xlsx)
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx",
                // Comma-separated values (.csv)
                "text/csv", ".csv",
                "text/plain", ".txt"
        );

        fileUploader.addSucceededListener(event -> {
            String fileName = event.getFileName();
            FILES_CACHE.put(fileName, memoryBuffer.getFileData(fileName));
            try (InputStream fileData = memoryBuffer.getInputStream(fileName)) {
                long contentLength = event.getContentLength();
                log.info("File {} uploaded: length = {} KB", fileName, contentLength / 1024);

                File tempFile = this.createTempFile(fileData, fileName);
                embeddingsService.processDocument(tempFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        fileUploader.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();
            Notification notification = Notification.show(
                    errorMessage,
                    2000,
                    Notification.Position.MIDDLE
            );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });
    }

    private void customizedUploaderLayout() {
        Paragraph hint = new Paragraph("File size must be <= 10 MB. Only PDF, Excel, CSV and TEXT files are accepted.");
        UploadI18N i18n = new UploadI18N();
        String supportedExtensions = Stream.of(DocumentType.values())
                .map(DocumentType::getExtensions)
                .flatMap(Collection::stream)
                .map(type -> "." + type)
                .collect(joining(", ", "(", ")."));
        i18n.setError(new UploadI18N.Error());
        i18n.getError().setIncorrectFileType("Please provide the file in one of the supported formats " + supportedExtensions);
        fileUploader.setI18n(i18n);
        Button uploadButton = new Button("Upload documents for AI assistant");
        uploadButton.setTooltipText("File size must be <= 10 MB. Only PDF, Excel, CSV and TEXT files are accepted.");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        fileUploader.setUploadButton(uploadButton);
        fileUploader.setWidthFull();
        fileUploader.getStyle().setTextAlign(Style.TextAlign.CENTER).setAlignItems(Style.AlignItems.CENTER);
        add(fileUploader);
    }

    // No methods to trigger render (refresh) - requires investigation
    private void copyValuesFromCache() {
        try {
            Field filesField = ReflectionUtils.findField(memoryBuffer.getClass(), "files");
            ReflectionUtils.makeAccessible(filesField);
            ReflectionUtils.setField(filesField, memoryBuffer, FILES_CACHE);
        } catch (Exception e) {
            log.error("Unable to copy files from cache", e);
        }
    }

    public File createTempFile(InputStream in, String fileNameWithExtension) throws IOException {
        String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'));
        String extension = fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf('.'));
        final File tempFile = File.createTempFile(fileName, extension);
        tempFile.deleteOnExit();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile))) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
}
