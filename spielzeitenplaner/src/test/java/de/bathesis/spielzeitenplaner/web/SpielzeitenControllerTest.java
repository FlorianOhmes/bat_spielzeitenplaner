package de.bathesis.spielzeitenplaner.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;


@WebMvcTest(SpielzeitenController.class)
class SpielzeitenControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die Startseite zur Spielzeitenberechnung ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten").andExpect(status().isOk());
    }

    @Test
    @DisplayName("Die Seite Kader der Spielzeitenberechnung ist erreichbar.")
    void test_02() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten/kader").andExpect(status().isOk());
    }

}
