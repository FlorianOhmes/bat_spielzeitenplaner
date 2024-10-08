package de.bathesis.spielzeitenplaner.services;

import java.util.Collection;
import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Team;


@Service
public class TeamService {

    private final TeamRepository teamRepo;

    public TeamService(TeamRepository teamRepo) {
        this.teamRepo = teamRepo;
    }

    public void save(String newTeamName) {
        Collection<Team> allEntries = teamRepo.findAll();
        if (allEntries.isEmpty()) {
            Team team = new Team(null, newTeamName);
            teamRepo.save(team);
        }
    }

}
