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
import de.bathesis.spielzeitenplaner.domain.Substitution;


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


    public List<Integer> calculateAllMinutes(List<Player> players, List<Double> totalScores) {
        List<Integer> minutes = new ArrayList<>(Collections.nCopies(players.size(), 0));
        Double maxScore = Collections.max(totalScores);

        for (int i = 0; i < players.size(); i++) {
            Double currentScore = totalScores.get(i);
            Integer calculatedMinutes = (int) Math.round((currentScore / maxScore) * 70);
            minutes.set(i, calculatedMinutes);
        }

        return minutes;
    }


    public List<Integer> calculatePlannedMinutes(List<Player> players, List<Substitution> substitutions) {
        List<Integer> plannedMinutes = new ArrayList<>(Collections.nCopies(11, 70));
        plannedMinutes.addAll(Collections.nCopies(players.size() - 11, 0));

        Substitution endOfGame = new Substitution(null, 70, "SPIEL", "ENDE");
        substitutions.add(endOfGame);

        Integer minutes;
        boolean isPlaying;
        Integer minuteIn;
        for (int i = 0; i < players.size(); i++) {

            minutes = 0;
            minuteIn = 0;
            if (i < 11) {
                isPlaying = true;
            } else {
                isPlaying = false;
            }

            String nameCurrentPlayer = players.get(i).getFirstName() + " " + players.get(i).getLastName();
            for (Substitution substitution : substitutions) {
                if (isPlaying == true && (substitution.getPlayerOut().equals(nameCurrentPlayer) || substitution.getMinute() == 70)) {
                    minutes += substitution.getMinute() - minuteIn;
                    isPlaying = false;
                }  else if (isPlaying == false && substitution.getPlayerIn().equals(nameCurrentPlayer)) {
                    isPlaying = true;
                    minuteIn = substitution.getMinute();
                }
            }

            plannedMinutes.set(i, minutes);

        }

        substitutions.remove(endOfGame);

        return plannedMinutes;
    }

}
