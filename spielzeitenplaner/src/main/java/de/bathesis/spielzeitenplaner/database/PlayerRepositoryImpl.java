package de.bathesis.spielzeitenplaner.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import de.bathesis.spielzeitenplaner.mapper.PlayerMapper;
import de.bathesis.spielzeitenplaner.services.PlayerRepository;
import de.bathesis.spielzeitenplaner.domain.Player;


@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    SpringDataPlayerRepository springRepository;

    public PlayerRepositoryImpl(SpringDataPlayerRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Collection<Player> findAll() {
        // Implementierung folgt !!! 
        return new ArrayList<>();
    }

    @Override
    public Optional<Player> findById(Integer id) {
        Optional<de.bathesis.spielzeitenplaner.database.Player> loaded = springRepository.findById(id);
        return loaded.map(PlayerMapper::toDomainPlayer);
    }
    
    @Override
    public Player save(Player domainPlayer) {
        de.bathesis.spielzeitenplaner.database.Player databasePlayer = PlayerMapper.toDatabasePlayer(domainPlayer);
        de.bathesis.spielzeitenplaner.database.Player saved = springRepository.save(databasePlayer);
        return PlayerMapper.toDomainPlayer(saved);
    }

    @Override
    public void deleteById(Integer playerId) {
        springRepository.deleteById(playerId);
    }

}
