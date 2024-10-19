package de.bathesis.spielzeitenplaner.services.repos;

import de.bathesis.spielzeitenplaner.domain.Assessment;
import java.util.Optional;


public interface AssessmentRepository {

    Assessment save(Assessment assessment);
    Optional<Assessment> findById(Integer id);

}
