package de.bathesis.spielzeitenplaner.database;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.mapper.TeamMapper;
import de.bathesis.spielzeitenplaner.services.TeamRepository;


@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final SpringDataTeamRepository springRepository;

    public TeamRepositoryImpl(SpringDataTeamRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Team save(Team domainTeam) {
        de.bathesis.spielzeitenplaner.database.Team databaseTeam = TeamMapper.toDatabaseTeam(domainTeam);
        de.bathesis.spielzeitenplaner.database.Team saved = springRepository.save(databaseTeam);
        return TeamMapper.toDomainTeam(saved);
    }

    @Override
    public Optional<Team> findById(Integer id) {
        Optional<de.bathesis.spielzeitenplaner.database.Team> loaded = springRepository.findById(id);
        return loaded.map(TeamMapper::toDomainTeam);
    }

    @Override
    public Collection<Team> findAll() {
        // TODO: Implementierung folgt... 
        return Collections.emptyList();
    }



}
