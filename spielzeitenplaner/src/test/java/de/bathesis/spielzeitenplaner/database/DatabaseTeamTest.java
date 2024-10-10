package de.bathesis.spielzeitenplaner.database;

import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.services.TeamRepository;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class DatabaseTeamTest {

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
    SpringDataTeamRepository springRepository;

    TeamRepository repository;

    @Autowired
    public DatabaseTeamTest(SpringDataTeamRepository springRepository) {
        this.repository = new TeamRepositoryImpl(springRepository);
    }


    @Test
    @DisplayName("Der Teamname kann gespeichert und geladen werden")
    public void test_01() {
        String teamName = "Spring Boot FC";
        Team team = new Team(null, teamName);

        Team saved = repository.save(team);
        Optional<Team> loaded = repository.findById(saved.id());

        assertThat(loaded.map(Team::name).orElseThrow()).isEqualTo(teamName);
    }

    @Test
    @DisplayName("Es werden alle Einträge in der Team-Tabelle gefunden.")
    public void test_02() {
        Team schalke = new Team(null, "FC Schalke 04");
        Team dortmund = new Team(null, "Borussia Dortmund");
        Team bayern = new Team(null, "FC Bayern München");
        Team savedSchalke = repository.save(schalke);
        Team savedDortmund = repository.save(dortmund);
        Team savedBayern = repository.save(bayern);

        Collection<Team> allEntries = repository.findAll();

        assertThat(allEntries).contains(savedSchalke, savedDortmund, savedBayern);
    }

}
