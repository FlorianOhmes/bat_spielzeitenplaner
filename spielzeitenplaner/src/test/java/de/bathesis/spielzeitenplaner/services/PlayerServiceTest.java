package de.bathesis.spielzeitenplaner.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.repos.PlayerRepository;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import java.util.Optional;


class PlayerServiceTest {

    PlayerRepository playerRepository = mock(PlayerRepository.class);

    PlayerService playerService = new PlayerService(playerRepository);

    @Test
    @DisplayName("Ein Spieler wird gelöscht.")
    void test_01() {
        Integer playerId = 17;
        playerService.deletePlayer(playerId);
        verify(playerRepository).deleteById(playerId);
    }

    @Test
    @DisplayName("Die Spielerliste wird geladen.")
    void test_02() {
        List<Player> players = TestObjectGenerator.generatePlayers();
        when(playerRepository.findAll()).thenReturn(players);

        List<Player> loadedPlayers = playerService.loadPlayers();

        verify(playerRepository).findAll();
        assertThat(loadedPlayers).containsExactlyInAnyOrderElementsOf(players);
    }

    @Test
    @DisplayName("Ein Spieler wird geladen.")
    void test_03() {
        Player player = new Player(37, "Thomas", "Müller", "ST", 25);
        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

        Player loadedPlayer = playerService.loadPlayer(player.getId());

        verify(playerRepository).findById(player.getId());
        assertThat(loadedPlayer).isEqualTo(loadedPlayer);
    }

    @Test
    @DisplayName("Wenn ein Spieler nicht gefunden wird oder nicht vorhanden ist, wird ein Dummy-Spieler zurückgegeben.")
    void test_04() {
        Player loadedPlayer = playerService.loadPlayer(null);

        verify(playerRepository, never()).findById(null);
        assertThat(loadedPlayer.getId()).isNull();
        assertThat(loadedPlayer.getFirstName()).isNull();
        assertThat(loadedPlayer.getLastName()).isNull();
        assertThat(loadedPlayer.getPosition()).isNull();
        assertThat(loadedPlayer.getJerseyNumber()).isNull();
    }

}
