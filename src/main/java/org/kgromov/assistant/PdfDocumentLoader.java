package org.kgromov.assistant;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PdfDocumentLoader implements DocumentLoader {

    @SneakyThrows
    @Override
    public Document loadDocument(Resource resource) {
        return FileSystemDocumentLoader.loadDocument(
                resource.getFile().toPath(),
                new ApachePdfBoxDocumentParser()
        );
    }

    @Override
    public boolean accept(DocumentFormat format) {
        return format == DocumentFormat.PDF;
    }
}
