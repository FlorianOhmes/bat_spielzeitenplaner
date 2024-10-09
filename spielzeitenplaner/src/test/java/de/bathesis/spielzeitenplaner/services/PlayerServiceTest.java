package de.bathesis.spielzeitenplaner.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


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

}
