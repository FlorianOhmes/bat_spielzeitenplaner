package de.bathesis.spielzeitenplaner.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.bathesis.spielzeitenplaner.domain.Player;


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
        // Implementierung folgt !!! 
        return new ArrayList<>(List.of(new Player(null, "Jan", "Oblak", "TW", 1)));
    }

}
