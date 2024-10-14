package de.bathesis.spielzeitenplaner.services.repos;

import java.util.Collection;
import java.util.Optional;
import de.bathesis.spielzeitenplaner.domain.Formation;


public interface FormationRepository {

    Collection<Formation> findAll();
    Formation save(Formation formation);
    Optional<Formation> findById(Integer id);

}
