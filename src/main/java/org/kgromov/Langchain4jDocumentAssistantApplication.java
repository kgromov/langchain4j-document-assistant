package org.kgromov;

import org.kgromov.config.EmbeddingStoreSettings;
import org.kgromov.config.OpenAISetting;
import org.kgromov.config.StorageSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({EmbeddingStoreSettings.class, OpenAISetting.class, StorageSettings.class})
public class Langchain4jDocumentAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(Langchain4jDocumentAssistantApplication.class, args);
	}

}
