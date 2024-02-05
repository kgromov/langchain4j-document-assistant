package org.kgromov.assistant;

import dev.langchain4j.data.document.Document;
import org.springframework.core.io.Resource;

import java.nio.file.Path;

public interface DocumentLoader {

    Document loadDocument(Path path);

    boolean accept(DocumentType type);
}
