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
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.domain.Setting;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;
import de.bathesis.spielzeitenplaner.services.repos.CriterionRepository;
import de.bathesis.spielzeitenplaner.services.repos.PlayerRepository;
import de.bathesis.spielzeitenplaner.services.repos.SettingRepository;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import java.util.Optional;
import java.util.Map;
import java.util.LinkedHashMap;


class PlayerServiceTest {

    PlayerRepository playerRepository = mock(PlayerRepository.class);

    AssessmentRepository assessmentRepository = mock(AssessmentRepository.class);
    SettingRepository settingRepository = mock(SettingRepository.class);
    CriterionRepository criterionRepository = mock(CriterionRepository.class);

    PlayerService playerService = new PlayerService(playerRepository, assessmentRepository, settingRepository, criterionRepository);

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
        Criterion criterion = new Criterion(2, "Sozialverhalten", "S", 0.25);
        Setting weeksGeneral = new Setting(1195, "weeksGeneral", 4.0);
        List<Assessment> assessments = new ArrayList<>(List.of(
            new Assessment(1, LocalDate.now(), criterion.getId(), playerId, 2.0),
            new Assessment(2, LocalDate.now(), criterion.getId(), playerId, 4.0),
            new Assessment(3, LocalDate.now(), criterion.getId(), playerId, 3.0)
        ));
        Double expectedScore = assessments.stream().mapToDouble(Assessment::getValue).average().orElseThrow();

        when(criterionRepository.findById(criterion.getId())).thenReturn(Optional.of(criterion));
        when(settingRepository.findById(weeksGeneral.getId())).thenReturn(Optional.of(weeksGeneral));
        when(assessmentRepository.findByPlayerIdAndCriterionIdAndDateAfter(
            playerId, criterion.getId(), LocalDate.now().minusWeeks(weeksGeneral.getValue().intValue()).minusDays(1))
        ).thenReturn(assessments);

        Double score = playerService.calculateScore(criterion.getId(), playerId);

        assertThat(score).isEqualTo(expectedScore);
    }

    @Test
    @DisplayName("Die calculateScore-Methode funktioniert auch für das spezielle Kriterium Trainingsbeteiligung korrekt.")
    void test_07() {
        Integer playerId = 1;
        Criterion criterion = new Criterion(1, "Trainingsbeteiligung", "T", 0.35);
        Setting weeksShortTerm = new Setting(1196, "weeksShortTerm", 2.0);
        Setting weightShortTerm = new Setting(1197, "weightShortTerm", 0.5);
        Setting weeksLongTerm = new Setting(1198, "weeksLongTerm", 12.0);
        Setting weightLongTerm = new Setting(1199, "weightLongTerm", 0.5);
        List<Assessment> assessmentsShortTerm = new ArrayList<>(List.of(
            new Assessment(1, LocalDate.now(), criterion.getId(), playerId, 0.0),
            new Assessment(2, LocalDate.now(), criterion.getId(), playerId, 1.0),
            new Assessment(3, LocalDate.now(), criterion.getId(), playerId, 0.0)
        ));
        List<Assessment> assessmentsLongTerm = new ArrayList<>(assessmentsShortTerm);
        assessmentsLongTerm.add(new Assessment(4, LocalDate.now(), criterion.getId(), playerId, 1.0));
        assessmentsLongTerm.add(new Assessment(5, LocalDate.now(), criterion.getId(), playerId, 1.0));
        assessmentsLongTerm.add(new Assessment(6, LocalDate.now(), criterion.getId(), playerId, 1.0));
        Double expectedScore = weightShortTerm.getValue() * (1.0 / 3.0) + weightLongTerm.getValue() * (4.0 / 6.0);

        when(criterionRepository.findById(criterion.getId())).thenReturn(Optional.of(criterion));
        when(settingRepository.findById(weeksShortTerm.getId())).thenReturn(Optional.of(weeksShortTerm));
        when(settingRepository.findById(weightShortTerm.getId())).thenReturn(Optional.of(weightShortTerm));
        when(settingRepository.findById(weeksLongTerm.getId())).thenReturn(Optional.of(weeksLongTerm));
        when(settingRepository.findById(weightLongTerm.getId())).thenReturn(Optional.of(weightLongTerm));
        when(assessmentRepository.findByPlayerIdAndCriterionIdAndDateAfter(
            playerId, criterion.getId(), LocalDate.now().minusWeeks(weeksShortTerm.getValue().intValue()).minusDays(1)
        )).thenReturn(assessmentsShortTerm);
        when(assessmentRepository.findByPlayerIdAndCriterionIdAndDateAfter(
            playerId, criterion.getId(), LocalDate.now().minusWeeks(weeksLongTerm.getValue().intValue()).minusDays(1)
        )).thenReturn(assessmentsLongTerm);


        Double score = playerService.calculateScore(criterion.getId(), playerId);

        assertThat(score).isEqualTo(expectedScore);
    }

    @Test
    @DisplayName("Die calculateScores-Methode funktioniert korrekt.")
    void test_08() {
        Integer playerId = 1;
        List<Criterion> criteria = new ArrayList<>(List.of(
            new Criterion(1, "Sozialverhalten", "S", 0.4),
            new Criterion(2, "Leistung", "L", 0.6)
        ));
        Setting weeksGeneral = new Setting(1195, "weeksGeneral", 4.0);
        List<Assessment> assessments = new ArrayList<>(List.of(
            new Assessment(1, LocalDate.now(), criteria.get(0).getId(), playerId, 2.0),
            new Assessment(2, LocalDate.now(), criteria.get(0).getId(), playerId, 2.0),
            new Assessment(3, LocalDate.now(), criteria.get(0).getId(), playerId, 5.0)
        ));
        List<Assessment> assessments2 = new ArrayList<>(List.of(
            new Assessment(4, LocalDate.now(), criteria.get(1).getId(), playerId, 5.0),
            new Assessment(5, LocalDate.now(), criteria.get(1).getId(), playerId, 5.0),
            new Assessment(6, LocalDate.now(), criteria.get(1).getId(), playerId, 5.0)
        ));


        LinkedHashMap<String, Double> expectedResult = new LinkedHashMap<>(Map.of(
            criteria.get(0).getName(), 3.0, 
            criteria.get(1).getName(), 5.0
        ));


        when(criterionRepository.findAll()).thenReturn(criteria);
        when(criterionRepository.findById(criteria.get(0).getId())).thenReturn(Optional.of(criteria.get(0)));
        when(criterionRepository.findById(criteria.get(1).getId())).thenReturn(Optional.of(criteria.get(1)));
        when(settingRepository.findById(weeksGeneral.getId())).thenReturn(Optional.of(weeksGeneral));
        when(assessmentRepository.findByPlayerIdAndCriterionIdAndDateAfter(
            playerId, criteria.get(0).getId(), LocalDate.now().minusWeeks(weeksGeneral.getValue().intValue()).minusDays(1))
        ).thenReturn(assessments);
        when(assessmentRepository.findByPlayerIdAndCriterionIdAndDateAfter(
            playerId, criteria.get(1).getId(), LocalDate.now().minusWeeks(weeksGeneral.getValue().intValue()).minusDays(1))
        ).thenReturn(assessments2);


        LinkedHashMap<String, Double> scores = playerService.calculateScores(playerId);

        assertThat(scores).isEqualTo(expectedResult);
    }

}
