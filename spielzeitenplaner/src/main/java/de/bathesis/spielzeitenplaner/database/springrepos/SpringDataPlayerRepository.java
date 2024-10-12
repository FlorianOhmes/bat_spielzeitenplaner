package de.bathesis.spielzeitenplaner.database.springrepos;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import de.bathesis.spielzeitenplaner.database.entities.Player;


public interface SpringDataPlayerRepository extends CrudRepository<Player, Integer> {

    Collection<Player> findAll();
    Optional<Player> findById(Integer id);
    Player save(Player player);
    void deleteById(Integer id);

}
