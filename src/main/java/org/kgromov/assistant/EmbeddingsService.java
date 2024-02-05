package org.kgromov.assistant;

import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmbeddingsService {
    private final EmbeddingStoreIngestor ingestor;
    private final List<DocumentLoader> documentLoaders;

    @SneakyThrows
    public void processDocument(Path filePath) {
        log.info("Start uploading to embeddings store...");
        var extension = FileNameUtils.getExtension(filePath);
        var documentFormat = DocumentType.from(extension)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported extension = " + extension));
        var documentLoader = documentLoaders.stream()
                .filter(loader -> loader.accept(documentFormat))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Document format is not supported yet"));
        ingestor.ingest(documentLoader.loadDocument(filePath));
        log.info("Finished uploading to embeddings store");
    }
}
