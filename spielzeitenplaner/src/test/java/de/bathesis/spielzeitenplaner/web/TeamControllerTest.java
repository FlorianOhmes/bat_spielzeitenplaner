package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die Seite zur Teamverwaltung ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/team").andExpect(status().isOk());
    }

    @Test
    @DisplayName("Die Seite Spieler bearbeiten/hinzufügen ist erreichbar.")
    void test_02() throws Exception {
        RequestHelper.performGet(mvc, "/team/player").andExpect(status().isOk());
    }

}
