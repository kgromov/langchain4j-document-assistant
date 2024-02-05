package org.kgromov.assistant;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class TextDocumentLoader implements DocumentLoader {

    @Override
    public Document loadDocument(Path path) {
        return FileSystemDocumentLoader.loadDocument(path, new TextDocumentParser()
        );
    }

    @Override
    public boolean accept(DocumentType type) {
        return type == DocumentType.TXT;
    }
}
