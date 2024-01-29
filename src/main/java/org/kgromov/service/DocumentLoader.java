package org.kgromov.service;

import dev.langchain4j.data.document.Document;
import org.springframework.core.io.Resource;

import java.io.File;

public interface DocumentLoader {
    Document loadDocument(Resource resource);

    boolean accept(DocumentFormat format);
}
