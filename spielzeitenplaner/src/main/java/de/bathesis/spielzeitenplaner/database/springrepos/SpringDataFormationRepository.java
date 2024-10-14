package de.bathesis.spielzeitenplaner.database.springrepos;

import org.springframework.data.repository.CrudRepository;
import de.bathesis.spielzeitenplaner.database.entities.Formation;


public interface SpringDataFormationRepository extends CrudRepository<Formation, Integer> {

    Formation save(Formation formation);

}
