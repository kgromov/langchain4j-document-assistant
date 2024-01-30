package org.kgromov.assistant;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExcelDocumentLoader implements DocumentLoader {

    @SneakyThrows
    @Override
    public Document loadDocument(Resource resource) {
        return FileSystemDocumentLoader.loadDocument(
                resource.getFile().toPath(),
                new ApachePoiDocumentParser()
        );
    }

    @Override
    public boolean accept(DocumentFormat format) {
        return format == DocumentFormat.EXCEL;
    }
}
