package de.bathesis.spielzeitenplaner.services;

import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.services.repos.FormationRepository;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


public class SettingsServiceTest {

    FormationRepository formationRepository = mock(FormationRepository.class);

    SettingsService settingsService = new SettingsService(formationRepository);


    @Test
    @DisplayName("Wenn noch kein Eintrag für die Formation vorhanden, wird diese gespeichert.")
    void test_01() {
        Formation formation = TestObjectGenerator.generateFormation();
        ArgumentCaptor<Formation> formationCaptor = ArgumentCaptor.forClass(Formation.class);
        when(formationRepository.findAll()).thenReturn(Collections.emptyList());

        settingsService.saveFormation(formation);

        verify(formationRepository).findAll();
        verify(formationRepository).save(formationCaptor.capture());
        assertThat(formationCaptor.getValue().getId()).isNull();
        assertThat(formationCaptor.getValue().getName()).isEqualTo(formation.getName());
        assertThat(formationCaptor.getValue().getPositions()).isEqualTo(formation.getPositions());
    }

    @Test
    @DisplayName("Wenn bereits ein Eintrag für die Formation vorhanden ist, wird der Name geupdated.")
    void test_02() {
        Formation formation = TestObjectGenerator.generateFormation();
        ArgumentCaptor<Formation> formationCaptor = ArgumentCaptor.forClass(Formation.class);
        when(formationRepository.findAll()).thenReturn(Collections.singletonList(formation));

        settingsService.saveFormation(formation);

        verify(formationRepository).findAll();
        verify(formationRepository).save(formationCaptor.capture());
        assertThat(formationCaptor.getValue().getId()).isEqualTo(formation.getId());
        assertThat(formationCaptor.getValue().getName()).isEqualTo(formation.getName());
        assertThat(formationCaptor.getValue().getPositions()).isEqualTo(formation.getPositions());
    }

    @Test
    @DisplayName("Das Formation-Objekt wird korrekt geladen.")
    void test_03() {
        Formation formation = TestObjectGenerator.generateFormation();
        when(formationRepository.findAll()).thenReturn(Collections.singletonList(formation));

        Formation loaded = settingsService.loadFormation();

        verify(formationRepository).findAll();
        assertThat(loaded.getId()).isEqualTo(formation.getId());
        assertThat(loaded.getName()).isEqualTo(formation.getName());
        assertThat(loaded.getPositions()).isEqualTo(formation.getPositions());
    }

}
