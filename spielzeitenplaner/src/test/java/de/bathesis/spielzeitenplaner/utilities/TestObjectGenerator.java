package de.bathesis.spielzeitenplaner.utilities;

import java.util.ArrayList;
import java.util.List;
import de.bathesis.spielzeitenplaner.domain.Position;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Player;


public class TestObjectGenerator {

    public static List<Player> generatePlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(null, "Jan", "Oblak", "TW", 1));
        players.add(new Player(null, "Ousmane", "Dembélé", "RF", 10));
        players.add(new Player(null, "Harry", "Kane", "ST", 9));
        players.add(new Player(null, "Cristiano", "Ronaldo", "LF", 7));
        players.add(new Player(null, "Mathijs", "De Ligt", "IV", 5));
        return players;
    }

    public static Formation generateFormation() {
        String name = "4-4-2";
        List<String> positionsAsString = new ArrayList<>(List.of(
            "TW", 
            "LV", "LIV", "RIV", "RV", 
            "LM", "LZM", "RZM", "RM", 
            "LS", "RS"
        ));
        List<Position> positions = positionsAsString.stream()
                                        .map(s -> new Position(null, s))
                                        .toList();
        return new Formation(null, name, positions);
    }

    public static List<Criterion> generateCriteria() {
        List<Criterion> criteria = new ArrayList<>();
        criteria.add(new Criterion(null, "Trainingsbeteiligung", "T", 0.35));
        criteria.add(new Criterion(null, "Leistung", "L", 0.35));
        criteria.add(new Criterion(null, "Sozialverhalten", "S", 0.2));
        criteria.add(new Criterion(null, "Engagement", "E", 0.1));
        return criteria;
    }

}
