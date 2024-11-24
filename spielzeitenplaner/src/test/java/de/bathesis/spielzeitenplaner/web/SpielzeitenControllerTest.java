package de.bathesis.spielzeitenplaner.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.domain.Position;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.services.SpielzeitenService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import de.bathesis.spielzeitenplaner.web.controller.SpielzeitenController;


@WebMvcTest(SpielzeitenController.class)
class SpielzeitenControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SpielzeitenService spielzeitenService;
    @MockBean
    PlayerService playerService;
    @MockBean
    SettingsService settingsService;

    List<Player> squad = TestObjectGenerator.generateSquad();


    @Test
    @DisplayName("Die Startseite zur Spielzeitenplanung ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten").andExpect(status().isOk())
                     .andExpect(view().name("/spielzeiten/start"));
    }

    @Test
    @DisplayName("Die Seite Kader der Spielzeitenplanung ist erreichbar.")
    void test_02() throws Exception {
        mvc.perform(getWithFlash())
           .andExpect(status().isOk())
           .andExpect(view().name("/spielzeiten/kader"));
    }

    @Test
    @DisplayName("Die Seite Startelf der Spielzeitenplanung ist erreichbar.")
    void test_03() throws Exception {
        when(settingsService.loadFormation()).thenReturn(TestObjectGenerator.generateFormation());

        mvc.perform(getWithFlashStartingXI()).andExpect(status().isOk())
           .andExpect(view().name("/spielzeiten/startingXI"));
    }

    @Test
    @DisplayName("Die Seite Wechsel eintragen der Spielzeitenplanung ist erreichbar.")
    void test_04() throws Exception {
        List<Integer> calculatedMinutes = new ArrayList<>(List.of(
            35, 70, 70, 70, 50, 70, 35, 55, 60, 65, 35, 35, 30, 25, 20, 15
        ));
        List<Integer> plannedMinutes = new ArrayList<>(List.of(
            45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60
        ));
        when(spielzeitenService.calculateAllMinutes(eq(squad), any())).thenReturn(calculatedMinutes);
        when(spielzeitenService.calculatePlannedMinutes(eq(squad), any())).thenReturn(plannedMinutes);

        mvc.perform(getWithSession())
           .andExpect(status().isOk())
           .andExpect(view().name("/spielzeiten/substitutions"));
    }

    @Test
    @DisplayName("Das Model für die Startseite der Spielzeitenplanung ist korrekt befüllt.")
    void test_05() throws Exception {
        List<Player> players = TestObjectGenerator.generatePlayers();
        when(playerService.loadPlayers()).thenReturn(players);

        RequestHelper.performGet(mvc, "/spielzeiten")
                     .andExpect(model().attribute("players", players))
                     .andExpect(model().attributeExists("totalScores"))
                     .andExpect(model().attributeExists("scoresCriterion1"))
                     .andExpect(model().attributeExists("scoresCriterion2"))
                     .andExpect(model().attribute("nameCriterion1" , ""))
                     .andExpect(model().attribute("nameCriterion2" , ""));
    }

    @Test
    @DisplayName("Es werden Post-Requests über /spielzeiten/determineKader akzeptiert und korrekt verarbeitet.")
    void test_06() throws Exception {
        List<Player> squad = TestObjectGenerator.generateSquad();
        List<Integer> ids = squad.stream().map(Player::getId).toList();
        when(spielzeitenService.determineSquad(ids)).thenReturn(squad);

        mvc.perform(post("/spielzeiten/determineKader")
                        .param("availablePlayers", 
                            squad.stream().map(p -> p.getId().toString()).collect(Collectors.joining(","))
                        )
                    )
           .andExpect(flash().attribute("squad", squad))
           .andExpect(flash().attribute("notInSquad", List.of()))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/spielzeiten/kader"));
    }

    @Test
    @DisplayName("Wenn weniger als 11 Spieler ausgewählt sind, wird eine Fehlermeldung ausgegeben.")
    void test_07() throws Exception {
        String errorMessage = "Es müssen mindestens 11 Spieler ausgewählt werden!";

        mvc.perform(post("/spielzeiten/determineKader"))
           .andExpect(flash().attribute("errorMessage", errorMessage))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/spielzeiten"));
    }

    @Test
    @DisplayName("Das Model für die Kader-Seite der Spielzeitenplanung ist korrekt befüllt.")
    void test_08() throws Exception {
        mvc.perform(getWithFlash())
           .andExpect(model().attributeExists("totalScoresSquad"))
           .andExpect(model().attributeExists("totalScoresNotInSquad"))
           .andExpect(model().attributeExists("scoresCriterion1Squad"))
           .andExpect(model().attributeExists("scoresCriterion1NotInSquad"))
           .andExpect(model().attributeExists("scoresCriterion2Squad"))
           .andExpect(model().attributeExists("scoresCriterion2NotInSquad"))
           .andExpect(model().attributeExists("scoresCriterion2NotInSquad"))
           .andExpect(model().attributeExists("nameCriterion1"))
           .andExpect(model().attributeExists("nameCriterion2"));
    }

    @Test
    @DisplayName("Es werden Post-Requests über /spielzeiten/determineStartingXI akzeptiert und korrekt verarbeitet.")
    void test_09() throws Exception {
        List<Player> startingXI = TestObjectGenerator.generateSquad().subList(0, 11);
        List<Integer> ids = startingXI.stream().map(Player::getId).toList();
        when(spielzeitenService.determineStartingXI(ids)).thenReturn(startingXI);

        mvc.perform(post("/spielzeiten/determineStartingXI")
                        .param("squadIds", 
                            ids.stream().map(id -> id.toString()).collect(Collectors.joining(","))
                        )
                    )
           .andExpect(flash().attribute("startingXI", startingXI))
           .andExpect(flash().attribute("bench", List.of()))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/spielzeiten/startingXI"));
    }

    @Test
    @DisplayName("Wenn weniger als 11 Spieler ausgewählt sind, wird eine Fehlermeldung ausgegeben.")
    void test_10() throws Exception {
        String errorMessage = "Es müssen mindestens 11 Spieler ausgewählt werden!";

        mvc.perform(post("/spielzeiten/determineStartingXI"))
           .andExpect(flash().attribute("errorMessage", errorMessage))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/spielzeiten/kader"));
    }

    @Test
    @DisplayName("Das Model für die Startelf-Seite ist korrekt befüllt.")
    void test_11() throws Exception {
        when(settingsService.loadFormation()).thenReturn(TestObjectGenerator.generateFormation());

        mvc.perform(getWithFlashStartingXI())
           .andExpect(model().attribute("numOfGK", 1))
           .andExpect(model().attribute("numOfDEF", 4))
           .andExpect(model().attribute("numOfMID", 4))
           .andExpect(model().attribute("numOfATK", 2))
           .andExpect(model().attributeExists("totalScoresStartingXI"))
           .andExpect(model().attributeExists("totalScoresBench"));
    }

    @Test
    @DisplayName("Es werden Post-Requests über /spielzeiten/updateStartingXI akzeptiert und korrekt verarbeitet.")
    void test_12() throws Exception {
        when(spielzeitenService.updateStartingXI(any(), any())).thenReturn(squad);
        when(settingsService.loadFormation()).thenReturn(TestObjectGenerator.generateFormation());

        mvc.perform(post("/spielzeiten/updateStartingXI")
                        .sessionAttr("startingXI", squad.subList(0, 11))
                        .sessionAttr("bench", squad.subList(11, squad.size()))
                        .param("changes", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16")
                    )
           .andExpect(flash().attribute("startingXI", squad.subList(0, 11)))
           .andExpect(flash().attribute("bench", squad.subList(11, squad.size())))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/spielzeiten/startingXI"));
    }

    @Test
    @DisplayName("Das Model für die Seite Wechsel eintragen ist korrekt befüllt.")
    void test_13() throws Exception {
        List<Integer> calculatedMinutes = new ArrayList<>(List.of(
            35, 70, 70, 70, 50, 70, 35, 55, 60, 65, 35, 35, 30, 25, 20, 15
        ));
        List<Integer> plannedMinutes = new ArrayList<>(List.of(
            45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60
        ));
        when(spielzeitenService.calculateAllMinutes(eq(squad), any())).thenReturn(calculatedMinutes);
        when(spielzeitenService.calculatePlannedMinutes(eq(squad), any())).thenReturn(plannedMinutes);

        mvc.perform(getWithSession())
           .andExpect(model().attributeExists("substitutions"))
           .andExpect(model().attribute("calculatedMinutes", calculatedMinutes))
           .andExpect(model().attribute("plannedMinutes", plannedMinutes));
    }

    @Test
    @DisplayName("Es werden Post-Requests über /spielzeiten/addSubstitution akzeptiert.")
    void test_14() throws Exception {
        mvc.perform(postWithSubstitutions())
           .andExpect(flash().attributeExists("substitutions"))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/spielzeiten/substitutions"));
    }

    @Test
    @DisplayName("Es werden Post-Requests über /spielzeiten/deleteSubstitution akzeptiert.")
    void test_15() throws Exception {
        mvc.perform(postWithSubstitutions())
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/spielzeiten/substitutions"));
    }


    private MockHttpServletRequestBuilder postWithSubstitutions() {
        return post("/spielzeiten/addSubstitution")
                        .sessionAttr("substitutions", new ArrayList<>());
    }

    private MockHttpServletRequestBuilder getWithFlash() {
        return get("/spielzeiten/kader")
                        .flashAttr("squad", squad)
                        .flashAttr("notInSquad", List.of());
    }

    private MockHttpServletRequestBuilder getWithFlashStartingXI() {
        return get("/spielzeiten/startingXI")
                        .flashAttr("startingXI", squad.subList(0, 11))
                        .flashAttr("bench", squad.subList(11, 16));
    }

    private MockHttpServletRequestBuilder getWithSession() {
        return get("/spielzeiten/substitutions")
                        .sessionAttr("numOfGK", 1).sessionAttr("numOfDEF", 4)
                        .sessionAttr("numOfMID", 4).sessionAttr("numOfATK", 2)
                        .sessionAttr("positions", TestObjectGenerator.generateFormation().getPositions().stream().map(Position::getName).toList())
                        .sessionAttr("startingXI", squad.subList(0, 11))
                        .sessionAttr("bench", squad.subList(11, 16))
                        .sessionAttr("totalScoresStartingXI", Collections.nCopies(11, 0.0))
                        .sessionAttr("totalScoresBench", Collections.nCopies(5, 0.0));
    }

}
