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
import de.bathesis.spielzeitenplaner.services.repos.TeamRepository;
import de.bathesis.spielzeitenplaner.web.forms.TeamForm;


class TeamServiceTest {

    TeamRepository teamRepo = mock(TeamRepository.class);

    TeamService teamService = new TeamService(teamRepo);


    @Test
    @DisplayName("Wenn noch kein Eintrag für den Teamnamen vorhanden, wird dieser gespeichert.")
    void test_01() {
        Team team = new Team(null, "Spring Boot FC");
        TeamForm teamForm = new TeamForm();
        teamForm.setName(team.name());
        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        when(teamRepo.findAll()).thenReturn(Collections.emptyList());

        teamService.save(teamForm);

        verify(teamRepo).findAll();
        verify(teamRepo).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().id()).isNull();
        assertThat(argumentCaptor.getValue().name()).isEqualTo(team.name());
    }

    @Test
    @DisplayName("Wenn bereits ein Eintrag für den Teamnamen vorhanden ist, wird der Name geupdated.")
    void test_02() {
        Team team = new Team(24, "FC Heidenheim");
        TeamForm teamForm = new TeamForm();
        teamForm.setName(team.name());
        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        when(teamRepo.findAll()).thenReturn(Collections.singletonList(team));

        teamService.save(teamForm);

        verify(teamRepo).findAll();
        verify(teamRepo).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().id()).isEqualTo(team.id());
        assertThat(argumentCaptor.getValue().name()).isEqualTo(team.name());
    }

    @Test
    @DisplayName("Das Team-Objekt wird korrekt geladen.")
    void test_03() {
        Team team = new Team(1020, "FC Heidenheim");
        when(teamRepo.findAll()).thenReturn(Collections.singletonList(team));

        Team loaded = teamService.load();

        verify(teamRepo).findAll();
        assertThat(loaded.id()).isEqualTo(team.id());
        assertThat(loaded.name()).isEqualTo(team.name());
    }

}
