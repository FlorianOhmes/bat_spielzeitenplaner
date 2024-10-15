package de.bathesis.spielzeitenplaner.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.repos.PlayerRepository;


@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void deletePlayer(Integer id) {
        playerRepository.deleteById(id);
    }

    public List<Player> loadPlayers() {
        Collection<Player> allPlayers = playerRepository.findAll();
        return new ArrayList<>(allPlayers);
    }

    public Player loadPlayer(Integer id) {
        Player noSuchPlayer = new Player(null, null, null, null, null);
        if (id == null) {return noSuchPlayer;}
        return playerRepository.findById(id).orElse(noSuchPlayer);
    }

    public void savePlayer(Player player) {
        // TODO: Implementierung folgt !!! 
    }

}
