package de.bathesis.spielzeitenplaner.services;

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
        formationRepository.saveFormation(formation);
    }

}
