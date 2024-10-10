package de.bathesis.spielzeitenplaner.database;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;


public interface SpringDataPlayerRepository extends CrudRepository<Player, Integer> {

    Collection<Player> findAll();
    Optional<Player> findById(Integer id);
    Player save(Player player);
    void deleteById(Integer id);

}
