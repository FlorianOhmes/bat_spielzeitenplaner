package de.bathesis.spielzeitenplaner.services.repos;

import java.util.Collection;
import java.util.Optional;
import de.bathesis.spielzeitenplaner.domain.Team;


public interface TeamRepository {

    Collection<Team> findAll();
    Optional<Team> findById(Integer id);
    Team save(Team team);

}
