package de.bathesis.spielzeitenplaner.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.services.repos.FormationRepository;


@Service
public class SettingsService {

    private final FormationRepository formationRepository;

    public SettingsService(FormationRepository formationRepository) {
        this.formationRepository = formationRepository;
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

}
