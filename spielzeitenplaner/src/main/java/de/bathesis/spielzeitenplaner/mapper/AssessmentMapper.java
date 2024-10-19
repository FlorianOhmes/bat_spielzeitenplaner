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

}
