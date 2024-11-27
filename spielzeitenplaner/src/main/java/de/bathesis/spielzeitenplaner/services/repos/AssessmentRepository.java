package de.bathesis.spielzeitenplaner.services.repos;

import de.bathesis.spielzeitenplaner.domain.Assessment;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;


public interface AssessmentRepository {

    Assessment save(Assessment assessment);
    Optional<Assessment> findById(Integer id);
    Collection<Assessment> findRelevantAssessmentsBy(Integer playerId, Integer criterionId, LocalDate date);

}
