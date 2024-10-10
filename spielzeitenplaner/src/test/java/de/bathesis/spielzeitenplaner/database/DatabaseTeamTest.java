package de.bathesis.spielzeitenplaner.database;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import de.bathesis.spielzeitenplaner.services.PlayerRepository;
import de.bathesis.spielzeitenplaner.domain.Player;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class DatabaseTeamTest {

    @Container
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpassword");

    @Autowired
    SpringDataPlayerRepository springRepository;

    PlayerRepository repository;

    @Autowired
    public DatabaseTeamTest(SpringDataPlayerRepository springRepository) {
        this.repository = new PlayerRepositoryImpl(springRepository);
    }


    @Test
    @DisplayName("Ein Spieler kann gelöscht werden.")
    public void test_01() {
        Player player = new Player(null, "Konrad", "Kaiser", "TW", 41);
        Player saved = repository.save(player);

        repository.deleteById(saved.getId());

        Optional<Player> loaded = repository.findById(saved.getId());

        assertThat(loaded).isEmpty();
    }

}
