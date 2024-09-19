package de.bathesis.spielzeitenplaner.templates;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.MainController;


@WebMvcTest(MainController.class)
class PlayerPageTest {

    @Autowired
    MockMvc mvc;

    Document playerPage;


    @BeforeEach
    void getPlayerPage() throws Exception {
        playerPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/team/player");
    }


    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String expectedTitle = "Spieler bearbeiten/hinzufügen";
        String pageTitle = RequestHelper.extractFrom(playerPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

}
