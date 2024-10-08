package de.bathesis.spielzeitenplaner.mapper;

import de.bathesis.spielzeitenplaner.domain.Team;

public class TeamMapper {

    public static Team toDomainTeam(de.bathesis.spielzeitenplaner.database.Team dbTeam) {
        return new Team(dbTeam.id(), dbTeam.name());
    }

    public static de.bathesis.spielzeitenplaner.database.Team toDatabaseTeam(Team domainTeam) {
        return new de.bathesis.spielzeitenplaner.database.Team(domainTeam.id(), domainTeam.name());
    }

}
