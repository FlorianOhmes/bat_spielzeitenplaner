package de.bathesis.spielzeitenplaner.mapper;

import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.web.forms.PlayerForm;


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

    public static PlayerForm toPlayerForm(Player player) {
        PlayerForm playerForm = new PlayerForm();
        playerForm.setId(player.getId());
        playerForm.setFirstName(player.getFirstName());
        playerForm.setLastName(player.getLastName());
        playerForm.setPosition(player.getPosition());
        playerForm.setJerseyNumber(player.getJerseyNumber());
        return playerForm;
    }

    public static Player toDomainPlayer(PlayerForm playerForm) {
        return new Player(
            playerForm.getId(), 
            playerForm.getFirstName(), playerForm.getLastName(), 
            playerForm.getPosition(), playerForm.getJerseyNumber()
        );
    }

}
