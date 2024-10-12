package de.bathesis.spielzeitenplaner.mapper;

import de.bathesis.spielzeitenplaner.domain.Player;


public class PlayerMapper {

    public static Player toDomainPlayer(de.bathesis.spielzeitenplaner.database.entities.Player dbPlayer) {
        return new Player(dbPlayer.id(), dbPlayer.firstName(), dbPlayer.lastName(), dbPlayer.position(), dbPlayer.jerseyNumber());
    }

    public static de.bathesis.spielzeitenplaner.database.entities.Player toDatabasePlayer(Player domainPlayer) {
        return new de.bathesis.spielzeitenplaner.database.entities.Player(
            domainPlayer.getId(), 
            domainPlayer.getFirstName(), domainPlayer.getLastName(), 
            domainPlayer.getPosition(), domainPlayer.getJerseyNumber()
        );
    }

}
