package de.bathesis.spielzeitenplaner.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import de.bathesis.spielzeitenplaner.domain.Team;


class TeamServiceTest {

    TeamRepository teamRepo = mock(TeamRepository.class);

    @Test
    @DisplayName("Wenn noch kein Eintrag für den Teamnamen vorhanden, wird dieser gespeichert.")
    void test_01() {
        String newTeamName = "Spring Boot FC";
        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        TeamService teamService = new TeamService(teamRepo);
        when(teamRepo.findAll()).thenReturn(Collections.emptyList());

        teamService.save(newTeamName);

        verify(teamRepo).findAll();
        verify(teamRepo).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().id()).isNull();
        assertThat(argumentCaptor.getValue().name()).isEqualTo(newTeamName);
    }

    @Test
    @DisplayName("Wenn bereits ein Eintrag für den Teamnamen vorhanden ist, wird der Name geupdated.")
    void test_02() {
        String newTeamName = "FC Heidenheim";
        Team team = new Team(24, newTeamName);
        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        TeamService teamService = new TeamService(teamRepo);
        when(teamRepo.findAll()).thenReturn(Collections.singletonList(team));

        teamService.save(newTeamName);

        verify(teamRepo).findAll();
        verify(teamRepo).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().id()).isEqualTo(team.id());
        assertThat(argumentCaptor.getValue().name()).isEqualTo(team.name());
    }

    @Test
    @DisplayName("Das Team-Objekt wird korrekt geladen.")
    void test_03() {
        TeamService teamService = new TeamService(teamRepo);
        Team team = new Team(1020, "FC Heidenheim");
        when(teamRepo.findAll()).thenReturn(Collections.singletonList(team));

        Team loaded = teamService.load();

        verify(teamRepo).findAll();
        assertThat(loaded.id()).isEqualTo(team.id());
        assertThat(loaded.name()).isEqualTo(team.name());
    }

}
