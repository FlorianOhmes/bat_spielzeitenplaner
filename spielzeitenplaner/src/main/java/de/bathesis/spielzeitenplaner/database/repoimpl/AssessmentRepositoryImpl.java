package de.bathesis.spielzeitenplaner.database.repoimpl;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import de.bathesis.spielzeitenplaner.database.springrepos.SpringDataAssessmentRepository;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.mapper.AssessmentMapper;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;


@Repository
public class AssessmentRepositoryImpl implements AssessmentRepository {

    private final SpringDataAssessmentRepository springRepository;

    public AssessmentRepositoryImpl(SpringDataAssessmentRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Assessment save(Assessment assessment) {
        de.bathesis.spielzeitenplaner.database.entities.Assessment databaseAssessment = 
            AssessmentMapper.toDatabaseAssessment(assessment);
        de.bathesis.spielzeitenplaner.database.entities.Assessment saved = springRepository.save(databaseAssessment);
        return AssessmentMapper.toDomainAssessment(saved);
    }

    @Override
    public Optional<Assessment> findById(Integer id) {
        Optional<de.bathesis.spielzeitenplaner.database.entities.Assessment> loaded = springRepository.findById(id);
        return loaded.map(AssessmentMapper::toDomainAssessment);
    }

    @Override
    public Collection<Assessment> findRelevantAssessmentsBy(Integer playerId, Integer criterionId, LocalDate date) {
        Collection<de.bathesis.spielzeitenplaner.database.entities.Assessment> loaded = 
            springRepository.findByPlayerIdAndCriterionIdAndDateAfter(playerId, criterionId, date);
        return loaded.stream().map(AssessmentMapper::toDomainAssessment).toList();
    }

}
