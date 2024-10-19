package de.bathesis.spielzeitenplaner.database.springrepos;

import org.springframework.data.repository.CrudRepository;
import de.bathesis.spielzeitenplaner.database.entities.Assessment;


public interface SpringDataAssessmentRepository extends CrudRepository<Assessment, Integer> {



}
