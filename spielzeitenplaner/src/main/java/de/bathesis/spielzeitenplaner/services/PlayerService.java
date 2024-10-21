package de.bathesis.spielzeitenplaner.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;
import de.bathesis.spielzeitenplaner.services.repos.CriterionRepository;
import de.bathesis.spielzeitenplaner.services.repos.PlayerRepository;
import de.bathesis.spielzeitenplaner.services.repos.SettingRepository;


@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final CriterionRepository criterionRepository;
    private final AssessmentRepository assessmentRepository;
    private final SettingRepository settingRepository;

    public PlayerService(PlayerRepository playerRepository, AssessmentRepository assessmentRepository, 
                         SettingRepository settingRepository, CriterionRepository criterionRepository) {
        this.playerRepository = playerRepository;
        this.assessmentRepository = assessmentRepository;
        this.settingRepository = settingRepository;
        this.criterionRepository = criterionRepository;
    }

    public void deletePlayer(Integer id) {
        playerRepository.deleteById(id);
    }

    public List<Player> loadPlayers() {
        Collection<Player> allPlayers = playerRepository.findAll();
        return new ArrayList<>(allPlayers);
    }

    public Player loadPlayer(Integer id) {
        Player noSuchPlayer = new Player(null, null, null, null, null);
        if (id == null) {return noSuchPlayer;}
        return playerRepository.findById(id).orElse(noSuchPlayer);
    }

    public Integer savePlayer(Player player) {
        Player saved = playerRepository.save(player);
        return saved.getId();
    }

    public LinkedHashMap<String, Double> calculateScores(Integer id) {
        // TODO: Implementierung folgt !!! 
        LinkedHashMap<String, Double> hashMap = new LinkedHashMap<>();
        hashMap.put("Trainingsbeteiligung", 7.4);
        hashMap.put("Leistung", 9.2);
        hashMap.put("Sozialverhalten", 9.6);
        hashMap.put("Engagement", 7.1);
        return hashMap;
    }

    public Double calculateScore(Integer criterionId, Integer playerId) {
        Optional<Criterion> criterion = criterionRepository.findById(criterionId);
        if (!criterion.isPresent()) {
            return 0.0;
        }

        if (criterion.get().getName().equals("Trainingsbeteiligung")) {
            return calculateScoreTraining(criterionId, playerId);
        }

        return calculateScoreOther(criterionId, playerId);
    }

    private Double calculateScoreTraining(Integer criterionId, Integer playerId) {
        Integer weeksShortTerm = settingRepository.findById(1196).get().getValue().intValue();
        Double weightShortTerm = settingRepository.findById(1197).get().getValue();
        Integer weeksLongTerm = settingRepository.findById(1198).get().getValue().intValue();
        Double weightLongTerm = settingRepository.findById(1199).get().getValue();

        LocalDate dateShortTerm = LocalDate.now().minusWeeks(weeksShortTerm).minusDays(1);
        LocalDate dateLongTerm = LocalDate.now().minusWeeks(weeksLongTerm).minusDays(1);

        Collection<Assessment> assessmentsShortTerm = 
            assessmentRepository.findByPlayerIdAndCriterionIdAndDateAfter(playerId, criterionId, dateShortTerm);
        Collection<Assessment> assessmentsLongTerm = 
            assessmentRepository.findByPlayerIdAndCriterionIdAndDateAfter(playerId, criterionId, dateLongTerm);

        Long countShortTerm = assessmentsShortTerm.stream().filter(a -> a.getValue() > 0.0).count();
        Long totalShortTerm = assessmentsShortTerm.stream().count();
        Double scoreShortTerm = (double) (countShortTerm) / (double) (totalShortTerm);

        Long countLongTerm = assessmentsLongTerm.stream().filter(a -> a.getValue() > 0.0).count();
        Long totalLongTerm = assessmentsLongTerm.stream().count();
        Double scoreLongTerm = (double) (countLongTerm) / (double) (totalLongTerm);

        Double score = weightShortTerm * scoreShortTerm + weightLongTerm * scoreLongTerm;
        return score;
    }

    private Double calculateScoreOther(Integer criterionId, Integer playerId) {
        Integer weeksToSubtract = settingRepository.findById(1195).get().getValue().intValue();
        LocalDate date = LocalDate.now().minusWeeks(weeksToSubtract).minusDays(1);

        Collection<Assessment> assessments = 
            assessmentRepository.findByPlayerIdAndCriterionIdAndDateAfter(playerId, criterionId, date);
        Double score = assessments.stream().mapToDouble(Assessment::getValue).average().orElse(0.0);

        return score;
    }

    public Double calculateTotalScore(Integer id) {
        // TODO: Implementierung folgt !!! 
        return 8.4;
    }

}
