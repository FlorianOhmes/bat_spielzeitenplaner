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
    @DisplayName("Die Startseite zur Spielzeitenplanung ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten").andExpect(status().isOk());
    }

    @Test
    @DisplayName("Die Seite Kader der Spielzeitenplanung ist erreichbar.")
    void test_02() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten/kader").andExpect(status().isOk());
    }

    @Test
    @DisplayName("Die Seite Startelf der Spielzeitenplanung ist erreichbar.")
    void test_03() throws Exception {
        RequestHelper.performGet(mvc, "/spielzeiten/startingXI").andExpect(status().isOk());
    }

}
