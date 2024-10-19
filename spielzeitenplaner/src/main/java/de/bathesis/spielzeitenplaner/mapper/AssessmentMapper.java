package de.bathesis.spielzeitenplaner.mapper;

import java.util.List;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.web.forms.RecapForm;


public class AssessmentMapper {

    public static List<Assessment> toDomainAssessments(RecapForm recapForm) {
        List<Assessment> assessments = recapForm.getAssessments().stream()
                .map(formAssessment -> new Assessment(
                    null, recapForm.getDate(), 
                    formAssessment.getCriterionId(), formAssessment.getPlayerId(), formAssessment.getValue()
                ))
                .toList();
        return assessments;
    }

    public static Assessment toDomainAssessment(de.bathesis.spielzeitenplaner.database.entities.Assessment databaseAssessment) {
        return new Assessment(
            databaseAssessment.id(), databaseAssessment.date(), 
            databaseAssessment.criterionId(), databaseAssessment.playerId(), databaseAssessment.value()
        );
    }

    public static de.bathesis.spielzeitenplaner.database.entities.Assessment toDatabaseAssessment(Assessment domainAssessment) {
        return new de.bathesis.spielzeitenplaner.database.entities.Assessment(
            domainAssessment.getId(), domainAssessment.getDate(), 
            domainAssessment.getCriterionId(), domainAssessment.getPlayerId(), domainAssessment.getValue()
        );
    }

}
