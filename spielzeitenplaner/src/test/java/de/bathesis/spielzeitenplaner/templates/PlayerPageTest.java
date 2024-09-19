package de.bathesis.spielzeitenplaner.templates;

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
import java.util.List;
import java.util.ArrayList;


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

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird die Navigationsleiste korrekt angezeigt.")
    void test_02() throws Exception {
        String navbarBrandText = RequestHelper.extractFrom(playerPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractFrom(playerPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(ExpectedElements.NAVBRAND_TEXT);
        assertThat(navigationItemsTerms).contains(ExpectedElements.FEATURES);
    }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Footer korrekt angezeigt.")
    void test_03() throws Exception {
        String footerText = RequestHelper.extractFrom(playerPage, "footer p");
        assertThat(footerText).isEqualTo(ExpectedElements.FOOTER_TEXT);
    }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Bereich Spieler-Daten korrekt angezeigt.")
    void test_04() throws Exception {
        String expectedCardTitle = "Spieler-Daten";
        List<String> expectedAttributes = new ArrayList<>(List.of(
            "Name", "Trikotnummer", "Position"
        ));

        String cardTitle = RequestHelper.extractFrom(playerPage, ".card.player-data .card-body .card-title");
        String playerInfo = RequestHelper.extractFrom(playerPage, ".card.player-data .card-body .player-info");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(playerInfo).contains(expectedAttributes);
    }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Button Spieler bearbeiten korrekt angezeigt.")
    void test_05() throws Exception {
        String expectedButtonLabel = "Spieler-Daten bearbeiten";
        String buttonLabel = RequestHelper.extractFrom(playerPage, ".card.player-data .card-body button");
        assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Bereich Spieler-Scores korrekt angezeigt.")
    void test_06() throws Exception {
        String expectedCardTitle = "Scores";
        String cardTitle = RequestHelper.extractFrom(playerPage, ".card.scores .card-body .card-title");
        Elements playerScores = playerPage.select(".card.scores .card-body .player-scores");
        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(playerScores).isNotEmpty();
    }

}
