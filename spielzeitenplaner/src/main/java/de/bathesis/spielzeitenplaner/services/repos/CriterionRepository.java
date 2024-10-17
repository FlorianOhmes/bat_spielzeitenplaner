package de.bathesis.spielzeitenplaner.services.repos;

import java.util.Collection;
import java.util.Optional;
import de.bathesis.spielzeitenplaner.domain.Criterion;


public interface CriterionRepository {

    Collection<Criterion> findAll();
    Criterion save(Criterion criterion);
    Optional<Criterion> findById(Integer id);

}
