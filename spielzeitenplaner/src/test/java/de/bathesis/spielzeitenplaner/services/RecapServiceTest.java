package de.bathesis.spielzeitenplaner.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;
import de.bathesis.spielzeitenplaner.services.repos.CriterionRepository;
import java.util.List;
import java.util.ArrayList;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import java.time.LocalDate;


public class RecapServiceTest {

    AssessmentRepository assessmentRepository = mock(AssessmentRepository.class);
    CriterionRepository criterionRepository = mock(CriterionRepository.class);

    RecapService recapService = new RecapService(assessmentRepository, criterionRepository);

    Criterion criterion = new Criterion(1, "Trainingsbeteiligung", "T", 0.4);
    Criterion criterion2 = new Criterion(2, "Leistung", "L", 0.4);
    Player player = new Player(1, "Hans", "Sarpei", "RV", 2);
    Player player2 = new Player(2, "Gerald", "Asamoah", "ST", 14);


    @BeforeEach
    void setUp() {
        when(criterionRepository.findByName(criterion.getName())).thenReturn(criterion);
    }


    @Test
    @DisplayName("Die Bewertungen werden gespeichert.")
    void test_01() {
        List<Assessment> assessments = new ArrayList<>(List.of(
            new Assessment(null, LocalDate.now(), criterion.getId(), player.getId(), 1.0), 
            new Assessment(null, LocalDate.now(), criterion.getId(), player2.getId(), 1.0), 
            new Assessment(null, LocalDate.now(), criterion2.getId(), player.getId(),3.0), 
            new Assessment(null, LocalDate.now(), criterion2.getId(), player.getId(), 4.0)
        ));
        ArgumentCaptor<Assessment> assessmentCaptor = ArgumentCaptor.forClass(Assessment.class);

        recapService.submitAssessments(assessments);

        verify(assessmentRepository, times(4)).save(assessmentCaptor.capture());

        List<Assessment> savedAssessments = assessmentCaptor.getAllValues();

        assertThat(savedAssessments).isEqualTo(assessments);
    }

    @Test
    @DisplayName("Bei einer Bewertung von 0.0 wird diese nur gespeichert, wenn sie zum Kriterium Trainingsbeteiligung gehört.")
    void test_02() {
        Assessment notToSave = new Assessment(3, LocalDate.now(), criterion2.getId(), player.getId(),0.0);
        List<Assessment> assessments = new ArrayList<>(List.of(
            new Assessment(1, LocalDate.now(), criterion.getId(), player.getId(), 0.0), 
            new Assessment(2, LocalDate.now(), criterion.getId(), player2.getId(), 1.0), 
            notToSave, 
            new Assessment(4, LocalDate.now(), criterion2.getId(), player.getId(), 4.0)
        ));
        ArgumentCaptor<Assessment> assessmentCaptor = ArgumentCaptor.forClass(Assessment.class);

        recapService.submitAssessments(assessments);

        verify(assessmentRepository, times(3)).save(assessmentCaptor.capture());

        List<Assessment> savedAssessments = assessmentCaptor.getAllValues();

        assertThat(savedAssessments).doesNotContain(notToSave);
    }

}
