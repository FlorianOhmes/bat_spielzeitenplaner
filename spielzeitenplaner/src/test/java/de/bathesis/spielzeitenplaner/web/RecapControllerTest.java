package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.RecapController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.ArrayList;
import java.util.List;


@WebMvcTest(RecapController.class)
class RecapControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    PlayerService playerService;

    @MockBean
    SettingsService settingsService;

    List<Player> players = new ArrayList<>(List.of(new Player(1787, "Sandro", "Wagner", "ST", 19)));
    List<Criterion> criteria = new ArrayList<>(List.of(new Criterion(989, "Training", "T", 0.4)));


    @Test
    @DisplayName("Die Startseite des Recap-Bereichs ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/recap")
                     .andExpect(status().isOk())
                     .andExpect(view().name("/recap/start"));
    }

    @Test
    @DisplayName("Die Recap-Seite ist erreichbar.")
    void test_02() throws Exception {
        when(playerService.loadPlayers()).thenReturn(players);
        when(settingsService.loadCriteria()).thenReturn(criteria);

        RequestHelper.performGet(mvc, "/recap/assess?players=1787")
                     .andExpect(status().isOk())
                     .andExpect(view().name("/recap/recap"))
                     .andExpect(model().attributeExists("recapForm"))
                     .andExpect(model().attributeExists("numberOfPlayers"))
                     .andExpect(model().attributeExists("numberOfCriteria"));

        verify(playerService).loadPlayers();
        verify(settingsService).loadCriteria();
    }

}
