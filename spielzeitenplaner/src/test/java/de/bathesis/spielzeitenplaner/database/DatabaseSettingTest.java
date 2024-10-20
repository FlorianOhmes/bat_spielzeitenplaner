package de.bathesis.spielzeitenplaner.database;

import static org.assertj.core.api.Assertions.assertThat;

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
import de.bathesis.spielzeitenplaner.database.repoimpl.SettingRepositoryImpl;
import de.bathesis.spielzeitenplaner.database.springrepos.SpringDataSettingRepository;
import de.bathesis.spielzeitenplaner.domain.Setting;
import de.bathesis.spielzeitenplaner.services.repos.SettingRepository;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class DatabaseSettingTest {

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
    SpringDataSettingRepository springRepository;

    SettingRepository settingRepository;

    @Autowired
    public DatabaseSettingTest(SpringDataSettingRepository springRepository) {
        this.settingRepository = new SettingRepositoryImpl(springRepository);
    }

    @Test
    @DisplayName("Eine Einstellung kann gespeichert und geladen werden.")
    void test_01() {
        Setting setting = new Setting(null, "weeksGeneral", 4.0);

        Setting saved = settingRepository.save(setting);
        Setting loaded = settingRepository.findById(saved.getId()).get();

        assertThat(loaded).isEqualTo(saved);
    }

}
