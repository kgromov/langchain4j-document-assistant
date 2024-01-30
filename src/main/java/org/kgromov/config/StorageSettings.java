package org.kgromov.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

@ConfigurationProperties(prefix = "settings.storage")
public record StorageSettings(String uploadFolder) {

    public Path resolveUploadFolderPath() {
        return Paths.get(uploadFolder);
    }
}
