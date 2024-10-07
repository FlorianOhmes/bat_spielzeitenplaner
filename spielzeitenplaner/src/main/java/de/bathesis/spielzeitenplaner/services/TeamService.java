package de.bathesis.spielzeitenplaner.services;

import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Team;


@Service
public class TeamService {

    private final TeamRepository teamRepo;

    public TeamService(TeamRepository teamRepo) {
        this.teamRepo = teamRepo;
    }

    public void save(String newTeamName) {
        Team team = new Team(null, newTeamName);
        teamRepo.save(team);
    }



}
