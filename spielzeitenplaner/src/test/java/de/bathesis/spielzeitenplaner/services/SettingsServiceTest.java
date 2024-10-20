package de.bathesis.spielzeitenplaner.services;

import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Setting;
import de.bathesis.spielzeitenplaner.services.repos.CriterionRepository;
import de.bathesis.spielzeitenplaner.services.repos.FormationRepository;
import de.bathesis.spielzeitenplaner.services.repos.SettingRepository;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


public class SettingsServiceTest {

    FormationRepository formationRepository = mock(FormationRepository.class);
    CriterionRepository criterionRepository = mock(CriterionRepository.class);
    SettingRepository settingRepository = mock(SettingRepository.class);

    SettingsService settingsService = new SettingsService(formationRepository, criterionRepository, settingRepository);


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

    @Test
    @DisplayName("Die Kriterien werden geladen.")
    void test_04() {
        List<Criterion> criteria = TestObjectGenerator.generateCriteria();
        when(criterionRepository.findAll()).thenReturn(criteria);

        List<Criterion> loadedCriteria = settingsService.loadCriteria();

        verify(criterionRepository).findAll();
        assertThat(loadedCriteria).isEqualTo(criteria);
    }

    @Test
    @DisplayName("Die Kriterien werden gespeichert.")
    void test_05() {
        List<Criterion> criteria = TestObjectGenerator.generateCriteria();
        settingsService.updateCriteria(criteria);
        verify(criterionRepository).saveAll(criteria);
    }

    @Test
    @DisplayName("Die Kriterien werden gelöscht.")
    void test_06() {
        List<Criterion> criteria = TestObjectGenerator.generateCriteria();
        settingsService.deleteCriteria(criteria);
        verify(criterionRepository).deleteAll(criteria);
    }

    @Test
    @DisplayName("Die Scores-Einstellungen werden geladen.")
    void test_07() {
        List<Setting> settings = new ArrayList<>(List.of(
            new Setting(1195, "weeksGeneral", 6.0), 
            new Setting(1196, "weeksShortTerm", 3.0), 
            new Setting(1197, "weightShortTerm", 0.5), 
            new Setting(1198, "weeksLongTerm", 12.0), 
            new Setting(1199, "weightLongTerm", 0.5)
        ));
        when(settingRepository.findById(settings.get(0).getId())).thenReturn(Optional.of(settings.get(0)));
        when(settingRepository.findById(settings.get(1).getId())).thenReturn(Optional.of(settings.get(1)));
        when(settingRepository.findById(settings.get(2).getId())).thenReturn(Optional.of(settings.get(2)));
        when(settingRepository.findById(settings.get(3).getId())).thenReturn(Optional.of(settings.get(3)));
        when(settingRepository.findById(settings.get(4).getId())).thenReturn(Optional.of(settings.get(4)));

        List<Setting> scoreSettings = settingsService.loadScoreSettings();

        assertThat(scoreSettings).containsExactlyElementsOf(settings);
    }

}
