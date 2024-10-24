package de.bathesis.spielzeitenplaner.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;


class SpielzeitenServiceTest {

    PlayerService playerService = mock(PlayerService.class);
    SettingsService settingsService = mock(SettingsService.class);

    SpielzeitenService spielzeitenService = new SpielzeitenService(playerService, settingsService);


    @Test
    @DisplayName("Der Kader wird korrekt berechnet.")
    void test_01() {
        Player player1 = new Player(1, "Lionel", "Messi", "RF", 10);
        Player player2 = new Player(2, "Luis", "Suarez", "ST", 9);
        Player player3 = new Player(3, "Neymar", "Jr.", "LF", 11);
        LinkedHashMap<String, Double> scoresPlayer1 = new LinkedHashMap<>(Map.of("Criterion1", 4.0));
        LinkedHashMap<String, Double> scoresPlayer2 = new LinkedHashMap<>(Map.of("Criterion1", 5.0));
        LinkedHashMap<String, Double> scoresPlayer3 = new LinkedHashMap<>(Map.of("Criterion1", 3.0));
        List<Player> team = List.of(player1, player2, player3);
        List<Integer> ids = List.of(player1.getId(), player2.getId(), player3.getId());

        when(playerService.loadPlayers()).thenReturn(team);
        when(playerService.calculateScores(player1.getId())).thenReturn(scoresPlayer1);
        when(playerService.calculateScores(player2.getId())).thenReturn(scoresPlayer2);
        when(playerService.calculateScores(player3.getId())).thenReturn(scoresPlayer3);
        when(playerService.calculateTotalScore(scoresPlayer1)).thenReturn(8.4);
        when(playerService.calculateTotalScore(scoresPlayer2)).thenReturn(9.2);
        when(playerService.calculateTotalScore(scoresPlayer3)).thenReturn(7.1);

        List<Player> squad = spielzeitenService.determineSquad(ids);

        assertThat(squad).containsExactly(player2, player1, player3);
    }

    @Test
    @DisplayName("Die Startelf wird korrekt berechnet.")
    void test_02() {
        Player player1 = new Player(1, "Vinicius", "Junior", "LS", 7);
        Player player2 = new Player(2, "Kylian", "Mbappe", "LS", 9);
        Player player3 = new Player(3, "Toni", "Kroos", "LZM", 8);
        LinkedHashMap<String, Double> scoresPlayer1 = new LinkedHashMap<>(Map.of("Criterion1", 4.0));
        LinkedHashMap<String, Double> scoresPlayer2 = new LinkedHashMap<>(Map.of("Criterion1", 5.0));
        LinkedHashMap<String, Double> scoresPlayer3 = new LinkedHashMap<>(Map.of("Criterion1", 3.0));
        Formation formation = TestObjectGenerator.generateFormation();
        List<Integer> ids = List.of(player1.getId(), player2.getId(), player3.getId());

        when(playerService.loadPlayer(player1.getId())).thenReturn(player1);
        when(playerService.loadPlayer(player2.getId())).thenReturn(player2);
        when(playerService.loadPlayer(player3.getId())).thenReturn(player3);
        when(playerService.calculateScores(player1.getId())).thenReturn(scoresPlayer1);
        when(playerService.calculateScores(player2.getId())).thenReturn(scoresPlayer2);
        when(playerService.calculateScores(player3.getId())).thenReturn(scoresPlayer3);
        when(playerService.calculateTotalScore(scoresPlayer1)).thenReturn(8.4);
        when(playerService.calculateTotalScore(scoresPlayer2)).thenReturn(9.2);
        when(playerService.calculateTotalScore(scoresPlayer3)).thenReturn(7.1);
        when(settingsService.loadFormation()).thenReturn(formation);

        List<Player> startingXI = spielzeitenService.determineStartingXI(ids);

        assertThat(startingXI.get(9)).isEqualTo(player2);
        assertThat(startingXI.get(10)).isEqualTo(player1);
        assertThat(startingXI.get(6)).isEqualTo(player3);
    }

}
