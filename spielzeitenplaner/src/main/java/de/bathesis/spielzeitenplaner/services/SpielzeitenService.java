package de.bathesis.spielzeitenplaner.services;

import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Player;
import java.util.Collections;


@Service
public class SpielzeitenService {

    private final PlayerService playerService;

    public SpielzeitenService(PlayerService playerService) {
        this.playerService = playerService;
    }


    public List<Player> determineSquad(List<Integer> idsAvailable) {
        return playerService.loadPlayers().stream()
                            .filter(p -> idsAvailable.contains(p.getId()))
                            .sorted((p1, p2) -> {
                                LinkedHashMap<String, Double> scores1 = playerService.calculateScores(p1.getId());
                                Double totalScore1 = playerService.calculateTotalScore(scores1);
                                LinkedHashMap<String, Double> scores2 = playerService.calculateScores(p2.getId());
                                Double totalScore2 = playerService.calculateTotalScore(scores2);
                                return totalScore2.compareTo(totalScore1);
                            })
                            .limit(16)
                            .toList();
    }


    public List<Player> determineStartingXI(List<Integer> squad) {
        // TODO: Implementierung folgt !!! 
        return Collections.emptyList();
    }

}
