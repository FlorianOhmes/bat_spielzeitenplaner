package de.bathesis.spielzeitenplaner.database;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.services.TeamRepository;


@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final SpringDataTeamRepository springRepository;

    public TeamRepositoryImpl(SpringDataTeamRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Team save(Team team) {
        Team saved = springRepository.save(team);
        return saved;
    }

    @Override
    public Optional<Team> findById(Integer id) {
        return springRepository.findById(id);
    }



}
