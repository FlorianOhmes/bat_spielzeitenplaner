package de.bathesis.spielzeitenplaner.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import de.bathesis.spielzeitenplaner.domain.Team;


class TeamServiceTest {

    TeamRepository teamRepo = mock(TeamRepository.class);

    @Test
    @DisplayName("Die save-Methode funktioniert korrekt.")
    void test_01() {
        Team newTeam = new Team("Spring Boot FC");
        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        TeamService teamService = new TeamService(teamRepo);

        teamService.save(newTeam.name());

        verify(teamRepo).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().name()).isEqualTo(newTeam.name());
    }

}
