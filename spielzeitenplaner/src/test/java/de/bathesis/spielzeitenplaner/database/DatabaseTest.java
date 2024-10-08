package de.bathesis.spielzeitenplaner.database;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.services.TeamRepository;


@DataJdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DatabaseTest {

    @Container
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpassword");

    @Autowired
    SpringDataTeamRepository springRepository;

    TeamRepository repository;

    @Autowired
    public DatabaseTest(SpringDataTeamRepository springRepository) {
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

}
