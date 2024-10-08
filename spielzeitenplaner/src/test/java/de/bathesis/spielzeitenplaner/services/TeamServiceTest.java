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
    @DisplayName("Die save-Methode funktioniert korrekt.")
    void test_02() {
        Team newTeam = new Team(1, "Spring Boot FC");
        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        TeamService teamService = new TeamService(teamRepo);

        teamService.save(newTeam.name());

        verify(teamRepo).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().name()).isEqualTo(newTeam.name());
    }

}
