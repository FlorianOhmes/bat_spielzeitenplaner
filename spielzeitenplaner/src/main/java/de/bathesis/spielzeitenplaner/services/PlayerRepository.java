package de.bathesis.spielzeitenplaner.services;

import java.util.Optional;
import de.bathesis.spielzeitenplaner.domain.Player;


public interface PlayerRepository {

    Optional<Player> findById(Integer id);
    Player save(Player player);
    void deleteById(Integer playerId);

}
