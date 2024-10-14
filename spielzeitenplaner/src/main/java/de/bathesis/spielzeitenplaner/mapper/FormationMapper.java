package de.bathesis.spielzeitenplaner.mapper;

import java.util.List;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Position;
import de.bathesis.spielzeitenplaner.web.forms.FormationForm;


public class FormationMapper {

    public static Formation toDomainFormation(FormationForm formationForm) {
        List<Position> positions = formationForm.getPositions().stream()
                                        .map(s -> new Position(null, s))
                                        .toList();
        return new Formation(null, formationForm.getName(), positions);
    }

    public static FormationForm toFormationForm(Formation domainFormation) {
        List<String> positions = domainFormation.getPositions().stream()
                                        .map(p -> p.getName())
                                        .toList();
        return new FormationForm(domainFormation.getName(), positions);
    }

    public static de.bathesis.spielzeitenplaner.database.entities.Formation toDatabaseFormation(Formation formation) {
        List<de.bathesis.spielzeitenplaner.database.entities.Position> positions = 
            formation.getPositions().stream()
                .map(p -> new de.bathesis.spielzeitenplaner.database.entities.Position(p.getId(), p.getName()))
                .toList();
        return new de.bathesis.spielzeitenplaner.database.entities.Formation(
            formation.getId(), formation.getName(), positions
        );
    }

    public static Formation toDomainFormation(de.bathesis.spielzeitenplaner.database.entities.Formation formation) {
        List<Position> positions = formation.positions().stream()
                                        .map(p -> new Position(p.id(), p.name()))
                                        .toList();
        return new Formation(
            formation.id(), formation.name(), positions
        );
    }

}
