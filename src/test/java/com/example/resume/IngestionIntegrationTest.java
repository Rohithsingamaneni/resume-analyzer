package com.example.resume;

import com.example.resume.service.IngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.Objects;

import org.junit.jupiter.api.Disabled;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Disabled("Requires a local Ollama instance running on port 11434 to generate embeddings")
class IngestionIntegrationTest {

    // Define the image name and tell Testcontainers it's a valid Postgres substitute
    private static final DockerImageName PG_VECTOR_IMAGE = DockerImageName
            .parse("ankane/pgvector:latest")
            .asCompatibleSubstituteFor("postgres");

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(PG_VECTOR_IMAGE) // <-- Use the object here
            .withDatabaseName("vector_db")
            .withUsername("postgres")
            .withPassword("password");

    @Autowired
    private IngestionService ingestionService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VectorStore vectorStore; // This is the generic interface

    @Test
    void testFullIngestionFlow() {
        ingestionService.ingest();

        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM vector_store", Integer.class);
        assertThat(count).isGreaterThan(0);

        System.out.println("✅ Success! Found " + count + " vector chunks in the database.");
    }

    @Test
    void testSearchRelevance() {
        ingestionService.ingest();
        var results = vectorStore.similaritySearch("Apple Maps search evaluation");
        assertThat(results).isNotEmpty();

        String actualText = Objects.requireNonNull(Objects.requireNonNull(results).getFirst().getText()).replaceAll("\\s+", " ");

        assertThat(actualText)
                .withFailMessage("The search found the document, but the expected keywords were missing or formatted poorly.")
                .contains("ranking-quality validation");

        System.out.println("Semantic Search verified! Found normalized text: " + actualText.substring(0, 50) + "...");
    }
}
