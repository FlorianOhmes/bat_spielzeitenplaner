package de.bathesis.spielzeitenplaner.database;

import org.springframework.data.repository.CrudRepository;
import java.util.Collection;
import java.util.Optional;


public interface SpringDataTeamRepository extends CrudRepository<Team, Integer> {

    Collection<Team> findAll();
    Optional<Team> findById(Integer id);
    Team save(Team team);

}
