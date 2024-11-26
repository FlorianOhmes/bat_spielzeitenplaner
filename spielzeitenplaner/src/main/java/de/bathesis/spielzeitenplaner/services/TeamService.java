package de.bathesis.spielzeitenplaner.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.mapper.TeamMapper;
import de.bathesis.spielzeitenplaner.services.repos.TeamRepository;
import de.bathesis.spielzeitenplaner.web.forms.TeamForm;


@Service
public class TeamService {

    private final TeamRepository teamRepo;

    public TeamService(TeamRepository teamRepo) {
        this.teamRepo = teamRepo;
    }

    public void save(TeamForm teamForm) {
        Collection<Team> allEntries = teamRepo.findAll();
        if (allEntries.isEmpty()) {
            Team team = TeamMapper.toDomainTeam(teamForm);
            teamRepo.save(team);
        } else {
            List<Team> entries = new ArrayList<>(allEntries);
            Team loaded = entries.get(0);
            Team newTeam = new Team(loaded.id(), teamForm.getName());
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
