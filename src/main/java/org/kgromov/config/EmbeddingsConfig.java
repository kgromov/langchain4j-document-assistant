package org.kgromov.config;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.cassandra.AstraDbEmbeddingConfiguration;
import dev.langchain4j.store.embedding.cassandra.AstraDbEmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;

@Configuration
@RequiredArgsConstructor
public class EmbeddingsConfig {
    private final EmbeddingStoreSettings embeddingsStore;
    private final OpenAISetting openai;

    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    public AstraDbEmbeddingConfiguration embeddingConfiguration() {
        return AstraDbEmbeddingConfiguration
                .builder()
                .token(embeddingsStore.dbToken())
                .databaseId(embeddingsStore.dbId())
                .databaseRegion(embeddingsStore.dbRegion())
                .keyspace(embeddingsStore.keyspace())
                .table("document_assistant")
                .dimension(384)
                .build();
    }

    @Profile("!test")
    @Qualifier("embeddingStore")
    @Bean
    public EmbeddingStore<TextSegment> astraDbEmbeddingStore(AstraDbEmbeddingConfiguration embeddingConfiguration) {
        return new AstraDbEmbeddingStore(embeddingConfiguration);
    }

    @Profile("test")
    @Qualifier("embeddingStore")
    @Bean
    public EmbeddingStore<TextSegment> inMemoryEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(EmbeddingStore<TextSegment> embeddingStore,
                                                         EmbeddingModel embeddingModel) {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(
                        300,
                        0,
                        new OpenAiTokenizer(GPT_3_5_TURBO))
                )
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }

    @Bean
    public ConversationalRetrievalChain conversationalRetrievalChain(EmbeddingStore<TextSegment> embeddingStore,
                                                                     EmbeddingModel embeddingModel) {
        return ConversationalRetrievalChain.builder()
                .chatLanguageModel(
                        OpenAiChatModel.builder()
                                .apiKey(openai.apiKey())
                                .timeout(Duration.ofSeconds(60))
                                .build()
                )
//                .chatMemory(TokenWindowChatMemory.withMaxTokens(300, new OpenAiTokenizer(GPT_3_5_TURBO)))
                .chatMemory( MessageWindowChatMemory.withMaxMessages(100))
                .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel, 3, 0.7))
                .build();
    }
}
