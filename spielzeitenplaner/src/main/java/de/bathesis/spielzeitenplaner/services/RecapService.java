package de.bathesis.spielzeitenplaner.services;

import java.util.List;
import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;
import de.bathesis.spielzeitenplaner.services.repos.CriterionRepository;


@Service
public class RecapService {

    private final AssessmentRepository assessmentRepository;
    private final CriterionRepository criterionRepository;

    public RecapService(AssessmentRepository assessmentRepository, CriterionRepository criterionRepository) {
        this.assessmentRepository = assessmentRepository;
        this.criterionRepository = criterionRepository;
    }


    public void submitAssessments(List<Assessment> assessments) {
        Criterion participation = criterionRepository.findByName("Trainingsbeteiligung");

        assessments.stream()
                   .filter(a -> 
                        (a.getCriterionId().equals(participation.getId())) || 
                        (!a.getValue().equals(0.0))
                   )
                   .forEach(assessmentRepository::save);
    }

}
