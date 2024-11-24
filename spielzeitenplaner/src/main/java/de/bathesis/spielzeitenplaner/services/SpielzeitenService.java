package de.bathesis.spielzeitenplaner.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Player;


@Service
public class SpielzeitenService {

    private final PlayerService playerService;
    private final SettingsService settingsService;

    public SpielzeitenService(PlayerService playerService, SettingsService settingsService) {
        this.playerService = playerService;
        this.settingsService = settingsService;
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


    public List<Player> determineStartingXI(List<Integer> squadIds) {
        List<Player> startingXI = new ArrayList<>(Collections.nCopies(11, null));
        List<Player> squad = squadIds.stream().map(playerService::loadPlayer).toList();
        List<Double> totalScores = squadIds.stream().map(playerService::calculateScores).map(playerService::calculateTotalScore).toList();
        List<String> positionen = settingsService.loadFormation().getPositions().stream().map(p -> p.getName()).toList();

        // nach totalScore sortieren
        List<Integer> indicesSorted = IntStream.range(0, squad.size()).boxed()
                        .sorted((i, j) -> Double.compare(totalScores.get(j), totalScores.get(i))).toList();
        List<Player> sortedSquad = indicesSorted.stream().map(squad::get).toList();

        // Startelf befüllen 
        for (int i = 0; i < 11; i++) {
            if (i >= sortedSquad.size()) {break;}
            Player currentPlayer = sortedSquad.get(i);
            Integer index = positionen.indexOf(currentPlayer.getPosition());
            if (index == -1) {index = i;}
            while (startingXI.get(index) != null) {
                index = (index + 1) % 11;
            }
            startingXI.set(index, currentPlayer);
        }

        return startingXI;
    }


    public List<Player> updateStartingXI(List<Player> players, List<Integer> changes) {
        List<Player> updatedPlayers = new ArrayList<>(players);
        Set<Integer> alreadyChanged = new HashSet<>();

        for (int i = 0; i < changes.size(); i++) {
            int newIndex = changes.get(i);
            if (i != newIndex && !alreadyChanged.contains(i)) {
                Player player = updatedPlayers.get(i);
                updatedPlayers.set(i, updatedPlayers.get(newIndex));
                updatedPlayers.set(newIndex, player);
                alreadyChanged.add(i);
                alreadyChanged.add(newIndex);
            }
        }

        return updatedPlayers;
    }


    public List<Integer> calculateAllMinutes(List<Player> players) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculateAllMinutes'");
    }

}
