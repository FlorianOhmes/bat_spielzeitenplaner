package de.bathesis.spielzeitenplaner.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;
import de.bathesis.spielzeitenplaner.services.repos.PlayerRepository;
import de.bathesis.spielzeitenplaner.services.repos.SettingRepository;


@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final AssessmentRepository assessmentRepository;
    private final SettingRepository settingRepository;

    public PlayerService(PlayerRepository playerRepository, AssessmentRepository assessmentRepository, 
                         SettingRepository settingRepository) {
        this.playerRepository = playerRepository;
        this.assessmentRepository = assessmentRepository;
        this.settingRepository = settingRepository;
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
