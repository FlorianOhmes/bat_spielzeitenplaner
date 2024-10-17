package de.bathesis.spielzeitenplaner.mapper;

import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.web.forms.CriteriaForm;
import de.bathesis.spielzeitenplaner.web.forms.FormCriterion;
import java.util.List;


public class CriteriaMapper {

    public static CriteriaForm toCriteriaForm(List<Criterion> criteria) {
        List<FormCriterion> criterionForms = criteria.stream().map(CriterionMapper::toCriterionForm).toList();
        CriteriaForm criteriaForm = new CriteriaForm();
        criteriaForm.setCriteria(criterionForms);
        return criteriaForm;
    }

    public static List<Criterion> toDomainCriteria(CriteriaForm criteriaForm) {
        return criteriaForm.getCriteria().stream().map(CriterionMapper::toDomainCriterion).toList();
    }

}
