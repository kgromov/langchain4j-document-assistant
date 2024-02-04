package org.kgromov;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.kgromov.config.EmbeddingStoreSettings;
import org.kgromov.config.OpenAISetting;
import org.kgromov.config.StorageSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Push
@SpringBootApplication
@EnableConfigurationProperties({EmbeddingStoreSettings.class, OpenAISetting.class, StorageSettings.class})
public class Langchain4jDocumentAssistantApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(Langchain4jDocumentAssistantApplication.class, args);
	}

}
