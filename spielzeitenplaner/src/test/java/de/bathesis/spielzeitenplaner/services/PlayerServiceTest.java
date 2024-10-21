package de.bathesis.spielzeitenplaner.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.domain.Setting;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;
import de.bathesis.spielzeitenplaner.services.repos.PlayerRepository;
import de.bathesis.spielzeitenplaner.services.repos.SettingRepository;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import java.util.Optional;


class PlayerServiceTest {

    PlayerRepository playerRepository = mock(PlayerRepository.class);

    AssessmentRepository assessmentRepository = mock(AssessmentRepository.class);
    SettingRepository settingRepository = mock(SettingRepository.class);

    PlayerService playerService = new PlayerService(playerRepository, assessmentRepository, settingRepository);

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

    @Test
    @DisplayName("Ein Spieler wird gespeichert.")
    void test_05() {
        Player player = new Player(1337, "Thomas", "Müller", "ST", 25);
        when(playerRepository.save(player)).thenReturn(player);

        Integer savedId = playerService.savePlayer(player);

        verify(playerRepository).save(player);
        assertThat(savedId).isEqualTo(player.getId());
    }

    @Test
    @DisplayName("Die calculateScore-Methode funktioniert korrekt.")
    void test_06() {
        Integer playerId = 1;
        Integer criterionId = 1;
        Setting weeksGeneral = new Setting(1195, "weeksGeneral", 4.0);
        List<Assessment> assessments = new ArrayList<>(List.of(
            new Assessment(1, LocalDate.now(), criterionId, playerId, 2.0),
            new Assessment(2, LocalDate.now(), criterionId, playerId, 4.0),
            new Assessment(3, LocalDate.now(), criterionId, playerId, 3.0)
        ));
        Double expectedScore = assessments.stream().mapToDouble(Assessment::getValue).average().orElseThrow();

        when(settingRepository.findById(weeksGeneral.getId())).thenReturn(Optional.of(weeksGeneral));
        when(assessmentRepository.findByPlayerIdLikeAndCriterionIdLikeAndDateBefore(
            playerId, criterionId, LocalDate.now().minusWeeks(weeksGeneral.getValue().intValue()).minusDays(1))
        ).thenReturn(assessments);

        Double score = playerService.calculateScore(criterionId, playerId);

        assertThat(score).isEqualTo(expectedScore);
    }

}
