package de.bathesis.spielzeitenplaner.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.bathesis.spielzeitenplaner.domain.Player;


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
        List<Player> players = new ArrayList<>(List.of(new Player(1023, "Jan", "Oblak", "TW", 1)));
        when(playerRepository.findAll()).thenReturn(players);

        List<Player> loadedPlayers = playerService.loadPlayers();

        verify(playerRepository).findAll();
        assertThat(loadedPlayers).containsExactlyInAnyOrderElementsOf(players);
    }

}
