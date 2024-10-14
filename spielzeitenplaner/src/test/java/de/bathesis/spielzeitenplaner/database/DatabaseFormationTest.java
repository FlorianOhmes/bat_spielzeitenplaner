package de.bathesis.spielzeitenplaner.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import de.bathesis.spielzeitenplaner.database.repoimpl.FormationRepositoryImpl;
import de.bathesis.spielzeitenplaner.database.springrepos.SpringDataFormationRepository;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Position;
import de.bathesis.spielzeitenplaner.services.repos.FormationRepository;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class DatabaseFormationTest {

    @Container
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpassword");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }


    @Autowired
    SpringDataFormationRepository springRepository;

    FormationRepository repository;

    @Autowired
    public DatabaseFormationTest(SpringDataFormationRepository springRepository) {
        this.repository = new FormationRepositoryImpl(springRepository);
    }


    @Test
    @DisplayName("Die Formation kann gespeichert und geladen werden.")
    void test_01() {
        Formation formation = TestObjectGenerator.generateFormation();

        Formation saved = repository.save(formation);
        Optional<Formation> loaded = repository.findById(saved.getId());
        List<Position> loadedPositions = loaded.map(Formation::getPositions).orElseThrow();

        assertThat(loaded.map(Formation::getName).orElseThrow()).isEqualTo(formation.getName());
        assertThat(loadedPositions).containsExactlyInAnyOrderElementsOf(saved.getPositions());
    }

}
