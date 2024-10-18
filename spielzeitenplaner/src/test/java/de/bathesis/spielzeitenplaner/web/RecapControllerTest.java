package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.RecapController;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(RecapController.class)
class RecapControllerTest {

    @Autowired
    MockMvc mvc;

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
        RequestHelper.performGet(mvc, "/recap/assess")
                     .andExpect(status().isOk())
                     .andExpect(view().name("/recap/recap"));
    }

}
