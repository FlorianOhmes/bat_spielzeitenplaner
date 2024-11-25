package de.bathesis.spielzeitenplaner.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.domain.Substitution;
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

    @Test
    @DisplayName("Die Aktualisierung der Startelf funktioniert korrekt.")
    void test_03() {
        List<Player> squad = TestObjectGenerator.generateSquad();
        List<Integer> changes = new ArrayList<>(List.of(
            10, 1, 8, 3, 4, 2, 6, 7, 5, 9, 0, 11, 12, 13, 14, 15, 16
        ));

        List<Player> updatedStartingXI = spielzeitenService.updateStartingXI(squad, changes);

        assertThat(updatedStartingXI).hasSize(squad.size());
        assertThat(updatedStartingXI.get(10)).isEqualTo(squad.get(0));
        assertThat(updatedStartingXI.get(0)).isEqualTo(squad.get(10));
        assertThat(updatedStartingXI.get(2)).isEqualTo(squad.get(5));
        assertThat(updatedStartingXI.get(5)).isEqualTo(squad.get(8));
        assertThat(updatedStartingXI.get(8)).isEqualTo(squad.get(2));
    }

    @Test
    @DisplayName("Die Berechnung der Spielminuten funktioniert korrekt.")
    void test_04() {
        List<Player> squad = TestObjectGenerator.generateSquad();
        List<Double> totalScores = new ArrayList<>(List.of(
            8.4, 9.2, 7.1, 8.7, 9.3, 7.6, 8.0, 9.0, 7.4, 8.6, 7.8, 8.9, 9.1, 7.5, 8.5, 7.9
        ));

        List<Integer> minutes = spielzeitenService.calculateAllMinutes(squad, totalScores);

        assertThat(minutes).hasSize(squad.size());
        assertThat(minutes.get(2)).isEqualTo(53);
        assertThat(minutes.get(4)).isEqualTo(70);
        assertThat(minutes.get(13)).isEqualTo(56);
    }

    @Test
    @DisplayName("Die Berechnung der geplanten Spielminuten funktioniert korrekt, wenn keine Wechsel vorhanden sind.")
    void test_05() {
        List<Player> squad = TestObjectGenerator.generateSquad();
        List<Substitution> substitutions = new ArrayList<>();
        List<Integer> expected = new ArrayList<>(List.of(
            70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 0, 0, 0, 0, 0
        ));

        List<Integer> plannedMinutes = spielzeitenService.calculatePlannedMinutes(squad, substitutions);

        assertThat(plannedMinutes).hasSize(squad.size());
        assertThat(plannedMinutes).isEqualTo(expected);

    }

}
