package de.bathesis.spielzeitenplaner.database.springrepos;

import org.springframework.data.repository.CrudRepository;
import de.bathesis.spielzeitenplaner.database.entities.Assessment;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;


public interface SpringDataAssessmentRepository extends CrudRepository<Assessment, Integer> {

    Assessment save(Assessment assessment);
    Optional<Assessment> findById(Integer id);
    Collection<Assessment> findByPlayerIdAndCriterionIdAndDateAfter(Integer playerId, Integer criterionId, LocalDate date);

}
