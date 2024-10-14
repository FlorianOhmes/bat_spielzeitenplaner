package de.bathesis.spielzeitenplaner.database.repoimpl;

import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import de.bathesis.spielzeitenplaner.database.springrepos.SpringDataFormationRepository;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.mapper.FormationMapper;
import de.bathesis.spielzeitenplaner.services.repos.FormationRepository;


@Repository
public class FormationRepositoryImpl implements FormationRepository {

    private final SpringDataFormationRepository springRepository;

    public FormationRepositoryImpl(SpringDataFormationRepository springRepository) {
        this.springRepository = springRepository;
    }


    @Override
    public Formation save(Formation formation) {
        de.bathesis.spielzeitenplaner.database.entities.Formation databaseFormation = 
            FormationMapper.toDatabaseFormation(formation);
        de.bathesis.spielzeitenplaner.database.entities.Formation saved = springRepository.save(databaseFormation);
        return FormationMapper.toDomainFormation(saved);
    }

    @Override
    public Optional<Formation> findById(Integer id) {
        Optional<de.bathesis.spielzeitenplaner.database.entities.Formation> loaded = springRepository.findById(id);
        return loaded.map(FormationMapper::toDomainFormation);
    }

    @Override
    public Collection<Formation> findAll() {
        Collection<de.bathesis.spielzeitenplaner.database.entities.Formation> allEntries = springRepository.findAll();
        return allEntries.stream().map(FormationMapper::toDomainFormation).toList();
    }

}
