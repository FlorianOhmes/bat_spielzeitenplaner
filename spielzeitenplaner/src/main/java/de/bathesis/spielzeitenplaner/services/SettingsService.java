package de.bathesis.spielzeitenplaner.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Setting;
import de.bathesis.spielzeitenplaner.services.repos.CriterionRepository;
import de.bathesis.spielzeitenplaner.services.repos.FormationRepository;
import de.bathesis.spielzeitenplaner.services.repos.SettingRepository;


@Service
public class SettingsService {

    private final FormationRepository formationRepository;
    private final CriterionRepository criterionRepository;
    private final SettingRepository settingRepository;

    public SettingsService(FormationRepository formationRepository, CriterionRepository criterionRepository, 
                           SettingRepository settingRepository) {
        this.formationRepository = formationRepository;
        this.criterionRepository = criterionRepository;
        this.settingRepository = settingRepository;
    }

    public Formation loadFormation() {
        Collection<Formation> allEntries = formationRepository.findAll();
        if (allEntries.isEmpty()) {
            return new Formation(null, null, Collections.emptyList());
        }
        Formation formation = new ArrayList<>(allEntries).get(0);
        return formation;
    }

    public void saveFormation(Formation formation) {
        Collection<Formation> allEntries = formationRepository.findAll();
        if (allEntries.isEmpty()) {
            formationRepository.save(formation);
        } else {
            List<Formation> entries = new ArrayList<>(allEntries);
            Formation loaded = entries.get(0);
            Formation newFormation = new Formation(loaded.getId(), formation.getName(), formation.getPositions());
            formationRepository.save(newFormation);
        }
    }

    public List<Criterion> loadCriteria() {
        Collection<Criterion> allEntries = criterionRepository.findAll();
        return new ArrayList<>(allEntries);
    }

    public void updateCriteria(List<Criterion> criteria) {
        criterionRepository.saveAll(criteria);
    }

    public void deleteCriteria(List<Criterion> criteria) {
        criterionRepository.deleteAll(criteria);
    }

    public List<Setting> loadScoreSettings() {
        List<Setting> scoreSettings = IntStream.range(1195, 1200)
                                               .mapToObj(i -> settingRepository.findById(i).get())
                                               .toList();
        return scoreSettings;
    }

    public void saveScoreSettings(List<Setting> settings) {
        settings.stream().forEach(settingRepository::save);
    }

}
