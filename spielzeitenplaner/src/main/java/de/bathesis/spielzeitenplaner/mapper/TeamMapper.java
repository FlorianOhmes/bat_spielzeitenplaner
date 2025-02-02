package de.bathesis.spielzeitenplaner.mapper;

import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.web.forms.TeamForm;


public class TeamMapper {

    public static Team toDomainTeam(de.bathesis.spielzeitenplaner.database.entities.Team dbTeam) {
        return new Team(dbTeam.id(), dbTeam.name());
    }
    
    public static de.bathesis.spielzeitenplaner.database.entities.Team toDatabaseTeam(Team domainTeam) {
        return new de.bathesis.spielzeitenplaner.database.entities.Team(domainTeam.id(), domainTeam.name());
    }

    public static Team toDomainTeam(TeamForm teamForm) {
        return new Team(null, teamForm.getName());
    }

    public static TeamForm toTeamForm(Team domainTeam) {
        TeamForm teamForm = new TeamForm();
        teamForm.setName(domainTeam.name());
        return teamForm;
    }

}
