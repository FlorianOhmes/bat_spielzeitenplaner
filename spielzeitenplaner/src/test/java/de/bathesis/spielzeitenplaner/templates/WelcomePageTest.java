package de.bathesis.spielzeitenplaner.templates;

import de.bathesis.spielzeitenplaner.web.WelcomeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(WelcomeController.class)
public class WelcomePageTest {

    @Autowired
    MockMvc mvc;

    Document welcomePage;

    @BeforeEach
    void getWelcomePage() throws Exception {
        String html = mvc.perform(get("/"))
                         .andReturn()
                         .getResponse()
                         .getContentAsString();
        welcomePage = Jsoup.parse(html);
    }

    @Test
    @DisplayName("Auf der Startseite wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String expectedTitle = "Willkommen beim SpielzeitenPlaner!";
        String pageTitle = extractFrom(welcomePage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Startseite wird der Jumbotron korrekt angezeigt.")
    void test_02() throws Exception {
        String expectedJumbotronText = "Der einfachste Weg, deine Jugendfußball-Spiele zu planen und zu verwalten. Nutze unsere Tools, um die Spielzeiten deiner Spieler aufgrund eigens festgelegter Kriterien zu planen.";
        String jumbotronText = extractFrom(welcomePage, ".jumbotron p.lead");
        assertThat(jumbotronText).isEqualTo(expectedJumbotronText);
    }

    @Test
    @DisplayName("Auf der Startseite wird die Navigationsleiste korrekt angezeigt.")
    void test_03() throws Exception {
        String expectedNavBrandText = "SpielzeitenPlaner";
        List<String> expectedNavigationTerms = new ArrayList<>(List.of(
            "Recap", 
            "Spielzeiten planen", 
            "Team verwalten", 
            "Einstellungen" 
        ));
        String navbarBrandText = extractFrom(welcomePage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = extractFrom(welcomePage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(expectedNavBrandText);
        assertThat(navigationItemsTerms).contains(expectedNavigationTerms);
    }

    @Test
    @DisplayName("Auf der Startseite werden Cards mit den Hauptfunktionen korrekt angezeigt.")
    void test_04() throws Exception {
        List<String> expectedFeatures = new ArrayList<>(List.of(
            "Recap", 
            "Spielzeiten planen", 
            "Team verwalten", 
            "Einstellungen" 
        ));
        String cardTitles = extractFrom(welcomePage, ".main-features .card .card-title");
        Elements cardTexts = welcomePage.select(".main-features .card .card-body .card-text");
        assertThat(cardTitles).contains(expectedFeatures);
        assertThat(cardTexts.size()).isEqualTo(expectedFeatures.size());
    }


    // ausgelagerte Methoden 
    private String extractFrom(Document welcomePage, String cssQuery) {
        return welcomePage.select(cssQuery).text();
    }

}
