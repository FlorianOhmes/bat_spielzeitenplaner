package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.mapper.PlayerMapper;
import de.bathesis.spielzeitenplaner.mapper.TeamMapper;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.TeamService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import de.bathesis.spielzeitenplaner.web.controller.TeamController;
import de.bathesis.spielzeitenplaner.web.forms.PlayerForm;
import de.bathesis.spielzeitenplaner.web.forms.TeamForm;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TeamService teamService;

    @MockBean
    PlayerService playerService;

    Player player = new Player(null, "Thibaut", "Curtois", "TW", 1);


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
        TeamForm teamForm = TeamMapper.toTeamForm(team);
        when(teamService.load()).thenReturn(team);

        List<Player> players = TestObjectGenerator.generatePlayers();
        when(playerService.loadPlayers()).thenReturn(players);

        List<Double> totalScores = List.of(9.4, 7.4, 8.8 ,8.9, 8.2);
        when(playerService.calculateTotalScore(any())).thenReturn(9.4).thenReturn(7.4)
                                                      .thenReturn(8.8).thenReturn(8.9)
                                                      .thenReturn(8.2);

        RequestHelper.performGet(mvc, "/team")
                     .andExpect(model().attribute("teamForm", teamForm))
                     .andExpect(model().attribute("players", players))
                     .andExpect(model().attribute("totalScores", totalScores));
    }

    @Test
    @DisplayName("Die Seite Spieler bearbeiten/hinzufügen ist erreichbar.")
    void test_03() throws Exception {
        Player noPlayer = new Player(null, null, null, null, null);
        when(playerService.loadPlayer(noPlayer.getId())).thenReturn(noPlayer);
        RequestHelper.performGet(mvc, "/team/player")
                     .andExpect(status().isOk())
                     .andExpect(view().name("team/player"));
    }

    @Test
    @DisplayName("Das Model für die Player-Seite ist korrekt befüllt.")
    void test_04() throws Exception {
        Player player = new Player(123, "Thomas", "Schütz", "LZDM", 28);
        when(playerService.loadPlayer(player.getId())).thenReturn(player);
        PlayerForm playerForm = PlayerMapper.toPlayerForm(player);

        LinkedHashMap<String, Double> scores = new LinkedHashMap<>(Map.of(
            "Trainingsbeteiligung", 7.6, 
            "Leistung", 9.2, 
            "Sozialverhalten", 9.6, 
            "Engagement", 7.1
        ));
        Double totalScore = 8.4;
        when(playerService.calculateScores(player.getId())).thenReturn(scores);
        when(playerService.calculateTotalScore(scores)).thenReturn(totalScore);

        RequestHelper.performGet(mvc, "/team/player?id=" + player.getId())
                     .andExpect(model().attribute("playerForm", playerForm))
                     .andExpect(model().attribute("scores", scores))
                     .andExpect(model().attribute("totalScore", totalScore));
    }

    @Test
    @DisplayName("Es werden Post-Requests über /team/teamname akzeptiert.")
    void test_05() throws Exception {
        mvc.perform(post("/team/teamname").param("name", "Spring Boot FC"))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/team"));
    }

    @Test
    @DisplayName("Bei Post-Requests über /team/teamname wird die save-Methode des TeamServices aufgerufen.")
    void test_06() throws Exception {
        Team team = new Team(null, "Spring Boot FC");
        mvc.perform(post("/team/teamname").param("name", team.name()));
        verify(teamService).save(team);
    }

    @Test
    @DisplayName("Der Teamname wird im TeamController validiert und mögliche Fehler auf der Team-Seite ausgegeben.")
    void test_07() throws Exception {
        String html = mvc.perform(post("/team/teamname").param("name", ""))
           .andExpect(model().attributeErrorCount("teamForm", 1))
           .andReturn().getResponse().getContentAsString();

        String html2 = mvc.perform(post("/team/teamname").param("name", """
                hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
                hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
                """))
           .andExpect(model().attributeErrorCount("teamForm", 1))
           .andReturn().getResponse().getContentAsString();

        Elements errors = Jsoup.parse(html).select(".error");
        Elements errors2 = Jsoup.parse(html2).select(".error");
        assertThat(errors).hasSize(1);
        assertThat(errors2).hasSize(1);
    }

    @Test
    @DisplayName("Es werden Post-Requests über /team/deletePlayer akzeptiert.")
    void test_08() throws Exception {
        mvc.perform(post("/team/deletePlayer"))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/team"));
    }

    @Test
    @DisplayName("Bei Post-Requests über /team/deletePlayer wird die deletePlayer-Methode des PlayerServices aufgerufen.")
    void test_09() throws Exception {
        Integer playerID = 12;
        mvc.perform(post("/team/deletePlayer").param("id", String.valueOf(playerID)));
        verify(playerService).deletePlayer(playerID);
    }

    @Test
    @DisplayName("Es werden Post-Requests über /team/savePlayer akzeptiert.")
    void test_10() throws Exception {
        mvc.perform(postSuccessful())
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/team/player?id=0"));
    }

    @Test
    @DisplayName("Bei Post-Requests über /team/savePlayer wird die save-Methode des PlayerServices aufgerufen.")
    void test_11() throws Exception {
        mvc.perform(postSuccessful());
        verify(playerService).savePlayer(player);
    }

    @Test
    @DisplayName("Das Spieler-Formular wird im TeamController validiert und mögliche Fehler werden auf der Spieler-Seite ausgegeben.")
    void test_12() throws Exception {
        String html = mvc.perform(post("/team/savePlayer")
                                    .param("firstName", "")
                                    .param("lastName", "")
                                    .param("position", ""))
                        .andExpect(model().attributeErrorCount("playerForm", 5))
                        .andReturn().getResponse().getContentAsString();
        String html2 = mvc.perform(post("/team/savePlayer")
                                    .param("firstName", "Joshua")
                                    .param("lastName", "Kimmich")
                                    .param("position", "RV")
                                    .param("jerseyNumber", "132"))
                        .andExpect(model().attributeErrorCount("playerForm", 1))
                        .andReturn().getResponse().getContentAsString();

        Elements errors = Jsoup.parse(html).select(".error");
        Elements errors2 = Jsoup.parse(html2).select(".error");
        assertThat(errors).hasSize(4);
        assertThat(errors2).hasSize(1);
    }


    private MockHttpServletRequestBuilder postSuccessful() {
        return post("/team/savePlayer")
                .param("firstName", player.getFirstName())
                .param("lastName", player.getLastName())
                .param("position", player.getPosition())
                .param("jerseyNumber", String.valueOf(player.getJerseyNumber()));
    }

}
