package de.bathesis.spielzeitenplaner.database;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collection;
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
import de.bathesis.spielzeitenplaner.database.repoimpl.CriterionRepositoryImpl;
import de.bathesis.spielzeitenplaner.database.springrepos.SpringDataCriterionRepository;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.services.repos.CriterionRepository;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class DatabaseCriterionTest {

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
    SpringDataCriterionRepository springRepository;

    CriterionRepository criterionRepository;

    @Autowired
    public DatabaseCriterionTest(SpringDataCriterionRepository springRepository) {
        this.criterionRepository = new CriterionRepositoryImpl(springRepository);
    }


    @Test
    @DisplayName("Ein Kriterium kann gespeichert und geladen werden.")
    void test_01() {
        Criterion criterion = TestObjectGenerator.generateCriteria().get(0);

        Criterion saved = criterionRepository.save(criterion);
        Criterion loaded = criterionRepository.findById(saved.getId()).get();

        assertThat(loaded.getName()).isEqualTo(criterion.getName());
        assertThat(loaded.getAbbrev()).isEqualTo(criterion.getAbbrev());
        assertThat(loaded.getWeight()).isEqualTo(criterion.getWeight());
    }

    @Test
    @DisplayName("Es werden alle Kriterien gefunden.")
    void test_02() {
        Criterion criterion1 = TestObjectGenerator.generateCriteria().get(1);
        Criterion criterion2 = TestObjectGenerator.generateCriteria().get(2);
        Criterion saved1 = criterionRepository.save(criterion1);
        Criterion saved2 = criterionRepository.save(criterion2);

        Collection<Criterion> allEntries = criterionRepository.findAll();

        assertThat(allEntries).contains(saved1, saved2);
    }

}
