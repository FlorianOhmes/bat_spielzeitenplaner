package de.bathesis.spielzeitenplaner.database;

import org.springframework.stereotype.Repository;
import de.bathesis.spielzeitenplaner.services.PlayerRepository;


@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    @Override
    public void deleteById(Integer playerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }



}
