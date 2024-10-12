package de.bathesis.spielzeitenplaner.mapper;

import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.web.forms.FormationForm;


public class FormationMapper {

    public static Formation toDomainFormation(FormationForm formationForm) {
        return new Formation(null, formationForm.getName(), formationForm.getPositions());
    }

    public static FormationForm toFormationForm(Formation domainFormation) {
        return new FormationForm(domainFormation.getName(), domainFormation.getPositions());
    }

}
