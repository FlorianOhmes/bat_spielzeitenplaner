package de.bathesis.spielzeitenplaner.services;

import java.util.List;
import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;


@Service
public class RecapService {

    private final AssessmentRepository assessmentRepository;

    public RecapService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }


    public void submitAssessments(List<Assessment> assessments) {
        assessments.stream().forEach(assessmentRepository::save);
    }

}
