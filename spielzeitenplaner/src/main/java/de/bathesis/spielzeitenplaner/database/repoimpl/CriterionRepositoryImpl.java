package de.bathesis.spielzeitenplaner.database.repoimpl;

import java.util.Collection;
import java.util.Collections;

import org.springframework.stereotype.Repository;

import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.services.repos.CriterionRepository;


@Repository
public class CriterionRepositoryImpl implements CriterionRepository {

    @Override
    public Collection<Criterion> findAll() {
        // TODO: Implementierung folgt !!! 
        return Collections.emptyList();
    }



}
