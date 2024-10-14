package de.bathesis.spielzeitenplaner.services;

import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.services.repos.FormationRepository;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


public class SettingsServiceTest {

    FormationRepository formationRepository = mock(FormationRepository.class);

    SettingsService settingsService = new SettingsService(formationRepository);


    @Test
    @DisplayName("Die Formation wird gespeichert.")
    void test_01() {
        Formation formation = TestObjectGenerator.generateFormation();
        ArgumentCaptor<Formation> formationCaptor = ArgumentCaptor.forClass(Formation.class);

        settingsService.saveFormation(formation);

        verify(formationRepository).save(formationCaptor.capture());
        assertThat(formationCaptor.getValue().getName()).isEqualTo(formation.getName());
        assertThat(formationCaptor.getValue().getPositions()).isEqualTo(formation.getPositions());
    }

}
