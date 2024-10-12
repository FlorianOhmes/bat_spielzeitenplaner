package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.RecapController;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RecapController.class)
class RecapControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die Seite Recap ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/recap").andExpect(status().isOk());
    }

}
