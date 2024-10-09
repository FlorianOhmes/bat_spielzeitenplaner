package de.bathesis.spielzeitenplaner.services;

import org.springframework.stereotype.Service;


@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void deletePlayer(Integer id) {
        playerRepository.deleteById(id);
    }

}
