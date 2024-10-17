package de.bathesis.spielzeitenplaner.database.springrepos;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import de.bathesis.spielzeitenplaner.database.entities.Criterion;


public interface SpringDataCriterionRepository extends CrudRepository<Criterion, Integer> {

    Collection<Criterion> findAll();
    Criterion save(Criterion criterion);
    Optional<Criterion> findById(Integer id);

}
