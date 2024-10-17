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

    public static de.bathesis.spielzeitenplaner.database.entities.Criterion toDatabaseCriterion(Criterion domainCriterion) {
        return new de.bathesis.spielzeitenplaner.database.entities.Criterion(
            domainCriterion.getId(), domainCriterion.getName(), domainCriterion.getAbbrev(), domainCriterion.getWeight()
        );
    }

    public static Criterion toDomainCriterion(de.bathesis.spielzeitenplaner.database.entities.Criterion databaseCriterion) {
        return new Criterion(
            databaseCriterion.id(), databaseCriterion.name(), databaseCriterion.abbrev(), databaseCriterion.weight()
        );
    }

}
