package org.kgromov.assistant;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ExcelDocumentLoader implements DocumentLoader {

    @Override
    public Document loadDocument(Path path) {
        return FileSystemDocumentLoader.loadDocument(path, new ApachePoiDocumentParser());
    }

    @Override
    public boolean accept(DocumentType type) {
        return type == DocumentType.EXCEL;
    }
}
