package org.kgromov.assistant;

import dev.langchain4j.data.document.Document;
import org.springframework.core.io.Resource;

public interface DocumentLoader {
    Document loadDocument(Resource resource);

    boolean accept(DocumentFormat format);
}
