package de.bathesis.spielzeitenplaner.services;

import java.util.Optional;
import de.bathesis.spielzeitenplaner.domain.Team;


public interface TeamRepository {

    Team save(Team team);
    Optional<Team> findById(Integer id);

}
