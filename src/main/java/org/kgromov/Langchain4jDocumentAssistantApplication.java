package org.kgromov;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import org.kgromov.config.EmbeddingStoreSettings;
import org.kgromov.config.OpenAISetting;
import org.kgromov.config.StorageSettings;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Scanner;

@SpringBootApplication
@EnableConfigurationProperties({EmbeddingStoreSettings.class, OpenAISetting.class, StorageSettings.class})
public class Langchain4jDocumentAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(Langchain4jDocumentAssistantApplication.class, args);
	}


	@Profile("test")
	@Bean
	ApplicationRunner interactiveChatRunner(ConversationalRetrievalChain retrievalChain) {
		return args -> {
			Scanner scanner = new Scanner(System.in);

			while (true) {
				System.out.print("User: ");
				String userMessage = scanner.nextLine();

				if ("exit".equalsIgnoreCase(userMessage)) {
					break;
				}

				String agentMessage = retrievalChain.execute(userMessage);
				System.out.println("Agent: " + agentMessage);
			}

			scanner.close();
		};
	}
}
