package de.bathesis.spielzeitenplaner.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import de.bathesis.spielzeitenplaner.services.repos.AssessmentRepository;
import java.util.List;
import java.util.ArrayList;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import java.time.LocalDate;


public class RecapServiceTest {

    AssessmentRepository assessmentRepository = mock(AssessmentRepository.class);

    RecapService recapService = new RecapService(assessmentRepository);

    Criterion criterion = new Criterion(1, "Trainingsbeteiligung", "T", 0.4);
    Criterion criterion2 = new Criterion(2, "Leistung", "L", 0.4);
    Player player = new Player(1, "Hans", "Sarpei", "RV", 2);
    Player player2 = new Player(2, "Gerald", "Asamoah", "ST", 14);


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

}
