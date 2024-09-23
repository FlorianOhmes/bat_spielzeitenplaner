package de.bathesis.spielzeitenplaner.templates.playingtimes;

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
class StartPageTest {

    @Autowired
    MockMvc mvc;

    Document startPage;


    @BeforeEach
    void getStartPage() throws Exception {
        startPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/spielzeiten");
    }


    @Test
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String expectedTitle = "Spielzeiten berechnen";
        String pageTitle = RequestHelper.extractFrom(startPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

}
