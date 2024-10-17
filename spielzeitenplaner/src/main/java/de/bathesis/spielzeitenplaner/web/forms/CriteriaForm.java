package de.bathesis.spielzeitenplaner.web.forms;

import java.util.List;
import jakarta.validation.Valid;


public class CriteriaForm {

    @Valid
    private List<FormCriterion> criteria;

    public List<FormCriterion> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<FormCriterion> criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return "CriteriaForm [criteria=" + criteria + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((criteria == null) ? 0 : criteria.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CriteriaForm other = (CriteriaForm) obj;
        if (criteria == null) {
            if (other.criteria != null)
                return false;
        } else if (!criteria.equals(other.criteria))
            return false;
        return true;
    }

}
