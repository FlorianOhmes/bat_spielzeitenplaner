package de.bathesis.spielzeitenplaner.database.repoimpl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import de.bathesis.spielzeitenplaner.database.springrepos.SpringDataCriterionRepository;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.mapper.CriterionMapper;
import de.bathesis.spielzeitenplaner.services.repos.CriterionRepository;


@Repository
public class CriterionRepositoryImpl implements CriterionRepository {

    private final SpringDataCriterionRepository springRepository;

    public CriterionRepositoryImpl(SpringDataCriterionRepository springRepository) {
        this.springRepository = springRepository;
    }


    @Override
    public Collection<Criterion> findAll() {
        Collection<de.bathesis.spielzeitenplaner.database.entities.Criterion> allEntries = springRepository.findAll();
        return allEntries.stream().map(CriterionMapper::toDomainCriterion).collect(Collectors.toList());
    }


    @Override
    public Criterion save(Criterion criterion) {
        de.bathesis.spielzeitenplaner.database.entities.Criterion databaseCriterion = 
            CriterionMapper.toDatabaseCriterion(criterion);
        de.bathesis.spielzeitenplaner.database.entities.Criterion saved = springRepository.save(databaseCriterion);
        return CriterionMapper.toDomainCriterion(saved);
    }


    @Override
    public Optional<Criterion> findById(Integer id) {
        Optional<de.bathesis.spielzeitenplaner.database.entities.Criterion> loaded = springRepository.findById(id);
        return loaded.map(CriterionMapper::toDomainCriterion);
    }


    @Override
    public void saveAll(List<Criterion> criteria) {
        // TODO: Implementierung folgt 
    }


    @Override
    public void deleteAll(List<Criterion> criteria) {
        // TODO: Implementierung folgt 
    }



}
