package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.services.TeamService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TeamService teamService;


    @Test
    @DisplayName("Die Seite zur Teamverwaltung ist erreichbar.")
    void test_01() throws Exception {
        when(teamService.load()).thenReturn(new Team(142, "Holstein Kiel"));

        RequestHelper.performGet(mvc, "/team")
                     .andExpect(status().isOk())
                     .andExpect(view().name("team/team"));
    }

    @Test
    @DisplayName("Das Model für die Team-Seite ist korrekt befüllt.")
    void test_02() throws Exception {
        Team team = new Team(144, "Spring Boot FC");
        when(teamService.load()).thenReturn(team);

        RequestHelper.performGet(mvc, "/team")
                     .andExpect(model().attribute("team", team));
    }

    @Test
    @DisplayName("Die Seite Spieler bearbeiten/hinzufügen ist erreichbar.")
    void test_03() throws Exception {
        RequestHelper.performGet(mvc, "/team/player")
                     .andExpect(status().isOk())
                     .andExpect(view().name("team/player"));
    }

    @Test
    @DisplayName("Es werden Post-Request über /team/teamname akzeptiert.")
    void test_04() throws Exception {
        mvc.perform(post("/team/teamname"))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/team"));
    }

    @Test
    @DisplayName("Bei Post-Request über /team/teamname wird die save-Methode des TeamServices aufgerufen.")
    void test_05() throws Exception {
        String teamName = "Spring Boot FC";
        mvc.perform(post("/team/teamname").param("teamName", teamName));
        verify(teamService).save(teamName);
    }

}
