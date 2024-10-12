package de.bathesis.spielzeitenplaner.database.springrepos;

import org.springframework.data.repository.CrudRepository;
import de.bathesis.spielzeitenplaner.database.entities.Team;
import java.util.Collection;
import java.util.Optional;


public interface SpringDataTeamRepository extends CrudRepository<Team, Integer> {

    Collection<Team> findAll();
    Optional<Team> findById(Integer id);
    Team save(Team team);

}
