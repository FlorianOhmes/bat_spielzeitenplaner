package de.bathesis.spielzeitenplaner.database;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    @DisplayName("Die Kriterien können gespeichert und geladen werden.")
    void test_01() {
        List<Criterion> criteria = new ArrayList<>(List.of(
            new Criterion(null, "Training", "T", 0.4), 
            new Criterion(null, "Leistung", "L", 0.4)
        ));

        List<Criterion> saved = criterionRepository.saveAll(criteria);
        Criterion loaded = criterionRepository.findById(saved.get(0).getId()).get();
        Criterion loaded2 = criterionRepository.findById(saved.get(1).getId()).get();

        assertThat(loaded).isEqualTo(saved.get(0));
        assertThat(loaded2).isEqualTo(saved.get(1));
    }

    @Test
    @DisplayName("Es werden alle Kriterien gefunden.")
    void test_02() {
        Criterion criterion1 = new Criterion(null, "Training", "T", 0.4);
        Criterion criterion2 = new Criterion(null, "Leistung", "L", 0.4);
        Criterion saved1 = criterionRepository.save(criterion1);
        Criterion saved2 = criterionRepository.save(criterion2);

        Collection<Criterion> allEntries = criterionRepository.findAll();

        assertThat(allEntries).contains(saved1, saved2);
    }

    @Test
    @DisplayName("Eine Liste von Kriterien kann gelöscht werden.")
    void test_03() {
        List<Criterion> criteria = new ArrayList<>(List.of(
            new Criterion(null, "Training", "T", 0.4), 
            new Criterion(null, "Leistung", "L", 0.4)
        ));
        List<Criterion> saved = criterionRepository.saveAll(criteria);
        assertThat(saved).hasSize(2);

        criterionRepository.deleteAll(saved);

        Collection<Criterion> allEntries = criterionRepository.findAll();

        assertThat(allEntries).isEmpty();
    }

    @Test
    @DisplayName("Ein Kriterium kann nach seinem Namen gefunden werden.")
    void test_04() {
        Criterion criterion = new Criterion(null, "Trainingsbeteiligung", "T", 0.4);
        Criterion saved = criterionRepository.save(criterion);

        Criterion loaded = criterionRepository.findById(saved.getId()).get();
        assertThat(loaded).isEqualTo(saved);

        Criterion foundByName = criterionRepository.findByName(criterion.getName());

        assertThat(foundByName).isEqualTo(saved);
    }

}
