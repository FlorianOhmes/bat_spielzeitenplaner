package de.bathesis.spielzeitenplaner.services.repos;

import java.util.Optional;
import de.bathesis.spielzeitenplaner.domain.Formation;


public interface FormationRepository {

    Formation save(Formation formation);
    Optional<Formation> findById(Integer id);

}
