package de.bathesis.spielzeitenplaner.templates.spielzeiten;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.utilities.ExpectedElements;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.SpielzeitenController;


@WebMvcTest(SpielzeitenController.class)
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
    void test_01() {
        String pageTitle = RequestHelper.extractTextFrom(startPage, "h1");
        assertThat(pageTitle).isEqualTo(ExpectedElements.spielzeitenTitle());
    }

    @Test
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird die Navigationsleiste angezeigt.")
    void test_02() {
        Elements navbar = RequestHelper.extractFrom(startPage, "nav");
        assertThat(navbar).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird der Footer angezeigt.")
    void test_03() {
        Elements footer = RequestHelper.extractFrom(startPage, "footer");
        assertThat(footer).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird ein Paragraph mit einer kurzen Erklärung angezeigt.")
    void test_04() {
        String leadText = RequestHelper.extractTextFrom(startPage, "p.lead");
        assertThat(leadText).isNotBlank();
    }

    @Test
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird der Bereich Spieler auswählen korrekt angezeigt.")
    void test_05() {
        String expectedButtonLabel = "Weiter zum Kader";

        Elements form = RequestHelper.extractFrom(startPage, "form#availablePlayers");
        Elements playerContainer = RequestHelper.extractFrom(startPage, "#allPlayers");
        String buttonLabel = RequestHelper.extractTextFrom(form, ".form-button button");

        assertThat(form).isNotEmpty();
        assertThat(playerContainer).isNotEmpty();
        assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    }

}
