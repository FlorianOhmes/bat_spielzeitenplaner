package de.bathesis.spielzeitenplaner.services.repos;

import java.util.Collection;
import java.util.Optional;
import de.bathesis.spielzeitenplaner.domain.Player;


public interface PlayerRepository {

    Collection<Player> findAll();
    Optional<Player> findById(Integer id);
    Player save(Player player);
    void deleteById(Integer playerId);

}
