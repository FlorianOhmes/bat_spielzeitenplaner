package de.bathesis.spielzeitenplaner.database.springrepos;

import java.util.Collection;
import org.springframework.data.repository.CrudRepository;
import de.bathesis.spielzeitenplaner.database.entities.Formation;


public interface SpringDataFormationRepository extends CrudRepository<Formation, Integer> {

    Collection<Formation> findAll();
    Formation save(Formation formation);

}
