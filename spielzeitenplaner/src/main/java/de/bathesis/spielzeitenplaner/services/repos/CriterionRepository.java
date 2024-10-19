package de.bathesis.spielzeitenplaner.services.repos;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import de.bathesis.spielzeitenplaner.domain.Criterion;


public interface CriterionRepository {

    Collection<Criterion> findAll();
    Optional<Criterion> findById(Integer id);
    Criterion findByName(String name);

    Criterion save(Criterion criterion);
    List<Criterion> saveAll(List<Criterion> criteria);

    void deleteAll(List<Criterion> criteria);

}
