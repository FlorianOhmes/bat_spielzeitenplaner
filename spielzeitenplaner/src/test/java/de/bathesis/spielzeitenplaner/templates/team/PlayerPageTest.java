package de.bathesis.spielzeitenplaner.templates.team;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.TeamService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.TeamController;
import java.util.List;
import java.util.ArrayList;


@WebMvcTest(TeamController.class)
class PlayerPageTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TeamService teamService;

    @MockBean
    PlayerService playerService;

    Document playerPage;


    @BeforeEach
    void getPlayerPage() throws Exception {
        playerPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/team/player");
    }


    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird die korrekte Überschrift angezeigt.")
    void test_01() {
        String expectedTitle = "Spieler bearbeiten/hinzufügen";
        String pageTitle = RequestHelper.extractTextFrom(playerPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird die Navigationsleiste angezeigt.")
    void test_02() {
        Elements navbar = RequestHelper.extractFrom(playerPage, "nav");
        assertThat(navbar).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Footer angezeigt.")
    void test_03() {
        Elements footer = RequestHelper.extractFrom(playerPage, "footer");
        assertThat(footer).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Bereich Spieler-Daten korrekt angezeigt.")
    void test_04() {
        String expectedCardTitle = "Spieler-Daten";
        List<String> expectedAttributes = new ArrayList<>(List.of(
            "Name", "Trikotnummer", "Position"
        ));

        String cardTitle = RequestHelper.extractTextFrom(playerPage, ".card.player-data .card-body .card-title");
        String playerInfo = RequestHelper.extractTextFrom(playerPage, ".card.player-data .card-body .player-info");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(playerInfo).contains(expectedAttributes);
    }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Button Spieler bearbeiten korrekt angezeigt.")
    void test_05() {
        String expectedButtonLabel = "Spieler-Daten bearbeiten";
        String buttonLabel = RequestHelper.extractTextFrom(playerPage, ".card.player-data .card-body button");
        assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Bereich Spieler-Scores korrekt angezeigt.")
    void test_06() {
        String expectedCardTitle = "Scores";
        String cardTitle = RequestHelper.extractTextFrom(playerPage, ".card.scores .card-body .card-title");
        Elements playerScores = RequestHelper.extractFrom(playerPage, ".card.scores .card-body .player-scores");
        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(playerScores).isNotEmpty();
    }

}
