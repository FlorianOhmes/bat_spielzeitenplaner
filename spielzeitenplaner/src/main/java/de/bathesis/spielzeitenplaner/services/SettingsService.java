package de.bathesis.spielzeitenplaner.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.services.repos.FormationRepository;


@Service
public class SettingsService {

    private final FormationRepository formationRepository;

    public SettingsService(FormationRepository formationRepository) {
        this.formationRepository = formationRepository;
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
        // TODO: Implementierung folgt !!! 
        List<Criterion> criteria = new ArrayList<>();
        criteria.add(new Criterion(1001, "Training", "T", 0.35));
        criteria.add(new Criterion(1002, "Leistung", "L", 0.35));
        criteria.add(new Criterion(1003, "Sozialverhalten", "S", 0.2));
        criteria.add(new Criterion(1004, "Engagement", "E", 0.1));
        return criteria;
    }

}
