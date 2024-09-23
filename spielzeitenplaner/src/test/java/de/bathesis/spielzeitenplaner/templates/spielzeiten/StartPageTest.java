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

    @Test
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird die Navigationsleiste korrekt angezeigt.")
    void test_02() throws Exception {
        String navbarBrandText = RequestHelper.extractFrom(startPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractFrom(startPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(ExpectedElements.NAVBRAND_TEXT);
        assertThat(navigationItemsTerms).contains(ExpectedElements.FEATURES);
    }

    @Test
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird der Footer korrekt angezeigt.")
    void test_03() throws Exception {
        String footerText = RequestHelper.extractFrom(startPage, "footer p");
        assertThat(footerText).isEqualTo(ExpectedElements.FOOTER_TEXT);
    }

    @Test
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird ein Paragraph mit einer kurzen Erklärung angezeigt.")
    void test_04() throws Exception {
        String leadText = RequestHelper.extractFrom(startPage, "p.lead");
        assertThat(leadText).isNotBlank();
    }

    @Test
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird der Bereich Spieler auswählen korrekt angezeigt.")
    void test_05() throws Exception {
        String expectedButtonLabel = "Weiter zur Startaufstellung";

        Elements form = startPage.select("form#availablePlayers");
        Elements playerContainer = startPage.select("#allPlayers");
        String buttonLabel = form.select(".form-button button").text();

        assertThat(form).isNotEmpty();
        assertThat(playerContainer).isNotEmpty();
        assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    }

}
