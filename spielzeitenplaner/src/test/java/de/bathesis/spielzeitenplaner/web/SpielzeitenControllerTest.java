package de.bathesis.spielzeitenplaner.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.domain.Player;
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


    @Test
    @DisplayName("Die Startseite zur Spielzeitenplanung ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten").andExpect(status().isOk())
                     .andExpect(view().name("/spielzeiten/start"));
    }

    @Test
    @DisplayName("Die Seite Kader der Spielzeitenplanung ist erreichbar.")
    void test_02() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten/kader").andExpect(status().isOk())
                     .andExpect(view().name("/spielzeiten/kader"));
    }

    @Test
    @DisplayName("Die Seite Startelf der Spielzeitenplanung ist erreichbar.")
    void test_03() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten/startingXI").andExpect(status().isOk())
                     .andExpect(view().name("/spielzeiten/startingXI"));
    }

    @Test
    @DisplayName("Die Seite Wechsel eintragen der Spielzeitenplanung ist erreichbar.")
    void test_04() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten/substitutions").andExpect(status().isOk())
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

}
