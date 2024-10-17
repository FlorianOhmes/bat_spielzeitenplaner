package de.bathesis.spielzeitenplaner.services.repos;

import java.util.Collection;
import de.bathesis.spielzeitenplaner.domain.Criterion;


public interface CriterionRepository {

    Collection<Criterion> findAll();

}
