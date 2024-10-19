package de.bathesis.spielzeitenplaner.database.repoimpl;

import org.springframework.stereotype.Repository;

import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;


@Repository
public class AssessmentRepositoryImpl implements AssessmentRepository {

    @Override
    public Assessment save(Assessment assessment) {
        // TODO: Implementierung folgt !!! 
        return new Assessment(null, null, null, null, null);
    }

}
