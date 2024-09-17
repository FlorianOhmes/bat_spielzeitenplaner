package de.bathesis.spielzeitenplaner.templates;

import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(MainController.class)
public class WelcomePageTest {

    @Autowired
    MockMvc mvc;

    Document welcomePage;
    public static final List<String> expectedFeatures = new ArrayList<>(List.of(
        "Recap", 
        "Spielzeiten planen", 
        "Team verwalten", 
        "Einstellungen" 
    ));


    @BeforeEach
    void getWelcomePage() throws Exception {
        welcomePage = RequestHelper.performGetAndParseWithJSoup(mvc, "/");
    }


    @Test
    @DisplayName("Auf der Startseite wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String expectedTitle = "Willkommen beim SpielzeitenPlaner!";
        String pageTitle = RequestHelper.extractFrom(welcomePage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Startseite wird der Jumbotron korrekt angezeigt.")
    void test_02() throws Exception {
        String expectedJumbotronText = "Der einfachste Weg, deine Jugendfußball-Spiele zu planen und zu verwalten. Nutze unsere Tools, um die Spielzeiten deiner Spieler aufgrund eigens festgelegter Kriterien zu planen.";
        String jumbotronText = RequestHelper.extractFrom(welcomePage, ".jumbotron p.lead");
        assertThat(jumbotronText).isEqualTo(expectedJumbotronText);
    }

    @Test
    @DisplayName("Auf der Startseite wird die Navigationsleiste korrekt angezeigt.")
    void test_03() throws Exception {
        String expectedNavBrandText = "SpielzeitenPlaner";
        String navbarBrandText = RequestHelper.extractFrom(welcomePage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractFrom(welcomePage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(expectedNavBrandText);
        assertThat(navigationItemsTerms).contains(expectedFeatures);
    }

    @Test
    @DisplayName("Auf der Startseite werden Cards mit den Hauptfunktionen korrekt angezeigt.")
    void test_04() throws Exception {
        String cardTitles = RequestHelper.extractFrom(welcomePage, ".main-features .card .card-title");
        Elements cardTexts = welcomePage.select(".main-features .card .card-body .card-text");
        assertThat(cardTitles).contains(expectedFeatures);
        assertThat(cardTexts.size()).isEqualTo(expectedFeatures.size());
    }

    @Test
    @DisplayName("Auf der Startseite wird der Bereich Neuigkeiten & Updates korrekt angezeigt.")
    void test_05() throws Exception {
        String expectedNewsTitle = "Neuigkeiten & Updates:";
        String newsTitle = RequestHelper.extractFrom(welcomePage, ".news h2");
        Elements newsBox = welcomePage.select(".news .news-box");
        assertThat(newsTitle).isEqualTo(expectedNewsTitle);
        assertThat(newsBox).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Startseite wird der Footer korrekt angezeigt.")
    void test_06() throws Exception {
        String expectedFooterText = "© 2024 SpielzeitenPlaner. Alle Rechte vorbehalten.";
        String footerText = RequestHelper.extractFrom(welcomePage, "footer p");
        assertThat(footerText).isEqualTo(expectedFooterText);
    }

}
