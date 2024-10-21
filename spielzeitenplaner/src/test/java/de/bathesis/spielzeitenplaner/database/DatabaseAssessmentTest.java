package de.bathesis.spielzeitenplaner.database;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import de.bathesis.spielzeitenplaner.database.repoimpl.AssessmentRepositoryImpl;
import de.bathesis.spielzeitenplaner.database.springrepos.SpringDataAssessmentRepository;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.util.Collection;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class DatabaseAssessmentTest {

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
    SpringDataAssessmentRepository springRepository;

    AssessmentRepository assessmentRepository;

    @Autowired
    public DatabaseAssessmentTest(SpringDataAssessmentRepository springRepository) {
        this.assessmentRepository = new AssessmentRepositoryImpl(springRepository);
    }


    @Test
    @DisplayName("Eine Bewertung kann gespeichert und geladen werden.")
    void test_01() {
        Assessment assessment = new Assessment(
            null, LocalDate.of(2022, 12, 31), 1, 1, 3.0
        );

        Assessment saved = assessmentRepository.save(assessment);
        Assessment loaded = assessmentRepository.findById(saved.getId()).get();

        assertThat(loaded).isEqualTo(saved);
    }

    @Test
    @DisplayName("Bewertungen können nach Datum, Spieler und Kriterium gefiltert werden.")
    void test_02() {
        Integer playerId = 1;
        Integer criterionId = 1;
        Assessment assessment1 = new Assessment(
            null, LocalDate.of(2022, 12, 31), criterionId, playerId, 5.0
        );
        Assessment assessment2 = new Assessment(
            null, LocalDate.of(2022, 12, 29), criterionId, playerId, 3.0
        );
        Assessment assessment3 = new Assessment(
            null, LocalDate.of(2022, 12, 31), criterionId, 2, 2.0
        );
        Assessment saved1 = assessmentRepository.save(assessment1);
        Assessment saved2 = assessmentRepository.save(assessment2);
        Assessment saved3 = assessmentRepository.save(assessment3);

        Collection<Assessment> found = assessmentRepository.findByPlayerIdAndCriterionIdAndDateAfter(
            playerId, criterionId, LocalDate.of(2022, 12, 1)
        );

        assertThat(found).containsExactly(saved1, saved2);
        assertThat(found).doesNotContain(saved3);
    }

}
