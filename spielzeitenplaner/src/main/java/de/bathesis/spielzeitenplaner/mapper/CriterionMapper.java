package de.bathesis.spielzeitenplaner.mapper;

import de.bathesis.spielzeitenplaner.web.forms.FormCriterion;
import de.bathesis.spielzeitenplaner.domain.Criterion;


public class CriterionMapper {

    public static FormCriterion toCriterionForm(Criterion criterion) {
        FormCriterion criterionForm =  new FormCriterion();
        criterionForm.setId(criterion.getId());
        criterionForm.setName(criterion.getName());
        criterionForm.setAbbrev(criterion.getAbbrev());
        criterionForm.setWeight(criterion.getWeight());
        return criterionForm;
    }

}
