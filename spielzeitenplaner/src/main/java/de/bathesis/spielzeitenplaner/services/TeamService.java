package de.bathesis.spielzeitenplaner.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Team;


@Service
public class TeamService {

    private final TeamRepository teamRepo;

    public TeamService(TeamRepository teamRepo) {
        this.teamRepo = teamRepo;
    }

    public void save(Team team) {
        Collection<Team> allEntries = teamRepo.findAll();
        if (allEntries.isEmpty()) {
            teamRepo.save(team);
        } else {
            List<Team> entries = new ArrayList<>(allEntries);
            Team loaded = entries.get(0);
            Team newTeam = new Team(loaded.id(), team.name());
            teamRepo.save(newTeam);
        }
    }

    public Team load() {
        Collection<Team> allEntries = teamRepo.findAll();
        if (allEntries.isEmpty()) {
            return new Team(null, null);
        }
        List<Team> entries = new ArrayList<>(allEntries);
        Team team = entries.get(0);
        return team;
    }

}
