package org.kgromov.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "settings.embeddings-store")
public record EmbeddingStoreSettings(String dbId, String dbToken, String dbRegion, String keyspace) {
}
