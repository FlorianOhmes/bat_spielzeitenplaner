package de.bathesis.spielzeitenplaner.database;

import org.springframework.data.repository.CrudRepository;
import de.bathesis.spielzeitenplaner.domain.Team;
import java.util.Optional;


public interface SpringDataTeamRepository extends CrudRepository<Team, Integer> {

    Optional<Team> findById(Integer id);
    Team save(Team team);

}
