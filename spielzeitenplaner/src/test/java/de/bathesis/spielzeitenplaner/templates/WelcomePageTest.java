package de.bathesis.spielzeitenplaner.templates;

import de.bathesis.spielzeitenplaner.utilities.ExpectedElements;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(MainController.class)
class WelcomePageTest {

    @Autowired
    MockMvc mvc;
    Document welcomePage;

    @BeforeEach
    void getWelcomePage() throws Exception {
        welcomePage = RequestHelper.performGetAndParseWithJSoup(mvc, "/");
    }


    @Test
    @DisplayName("Auf der Startseite wird die korrekte Überschrift angezeigt.")
    void test_01() {
        String expectedTitle = "Willkommen beim SpielzeitenPlaner!";
        String pageTitle = RequestHelper.extractTextFrom(welcomePage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Startseite wird der Jumbotron korrekt angezeigt.")
    void test_02() {
        String expectedJumbotronText = "Der einfachste Weg, deine Jugendfußball-Spiele zu planen und zu verwalten. Nutze unsere Tools, um die Spielzeiten deiner Spieler aufgrund eigens festgelegter Kriterien zu planen.";
        String jumbotronText = RequestHelper.extractTextFrom(welcomePage, ".jumbotron p.lead");
        assertThat(jumbotronText).isEqualTo(expectedJumbotronText);
    }

    @Test
    @DisplayName("Auf der Startseite wird die Navigationsleiste angezeigt.")
    void test_03() {
        Elements navbar = RequestHelper.extractFrom(welcomePage, "nav");
        assertThat(navbar).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Startseite werden Cards mit den Hauptfunktionen korrekt angezeigt.")
    void test_04() {
        String cardTitles = RequestHelper.extractTextFrom(welcomePage, ".main-features .card .card-title");
        Elements cardTexts = RequestHelper.extractFrom(welcomePage, ".main-features .card .card-body .card-text");
        assertThat(cardTitles).contains(ExpectedElements.features());
        assertThat(cardTexts.size()).isEqualTo(ExpectedElements.features().size());
    }

    @Test
    @DisplayName("Auf der Startseite wird der Bereich Neuigkeiten & Updates korrekt angezeigt.")
    void test_05() {
        String expectedNewsTitle = "Neuigkeiten & Updates:";
        String newsTitle = RequestHelper.extractTextFrom(welcomePage, ".news h2");
        Elements newsBox = RequestHelper.extractFrom(welcomePage, ".news .news-box");
        assertThat(newsTitle).isEqualTo(expectedNewsTitle);
        assertThat(newsBox).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Startseite wird der Footer angezeigt.")
    void test_06() {
        Elements footer = RequestHelper.extractFrom(welcomePage, "footer");
        assertThat(footer).isNotEmpty();
    }

}
