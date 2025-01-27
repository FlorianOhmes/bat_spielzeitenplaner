package de.bathesis.spielzeitenplaner.templates.team;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.TeamService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.TeamController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.LinkedHashMap;


@WebMvcTest(TeamController.class)
class PlayerPageTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TeamService teamService;

    @MockBean
    PlayerService playerService;

    Document playerPage;

    Player player = new Player(14, "Cristiano", "Ronaldo", "LF", 7);
    LinkedHashMap<String, Double> scores = new LinkedHashMap<>(Map.of(
            "Trainingsbeteiligung", 9.2, 
            "Leistung", 9.5, 
            "Sozialverhalten", 8.8, 
            "Engagement", 8.4
        ));


    @BeforeEach
    void getPlayerPage() throws Exception {
        when(playerService.loadPlayer(player.getId())).thenReturn(player);
        when(playerService.calculateScores(player.getId())).thenReturn(scores);
        playerPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/team/player?id=" + player.getId());
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

    // @Test
    // @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Bereich Spieler-Daten korrekt angezeigt.")
    // void test_04() {
    //     String expectedCardTitle = "Spieler-Daten";
    //     List<String> expectedAttributes = new ArrayList<>(List.of(
    //         "Vorname", "Nachname", "Trikotnummer", "Position"
    //     ));

    //     String cardTitle = RequestHelper.extractTextFrom(playerPage, ".card.player-data .card-body .card-title");
    //     String playerInfo = RequestHelper.extractTextFrom(playerPage, ".card.player-data .card-body .player-info");

    //     assertThat(cardTitle).isEqualTo(expectedCardTitle);
    //     assertThat(playerInfo).contains(expectedAttributes);
    // }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird der Bereich Spieler-Daten korrekt angezeigt.")
    void test_04() {
        String expectedCardTitle = "Spieler-Daten";
        Elements sectionPlayerData = RequestHelper.extractFrom(playerPage, ".card.player-data");
        Elements h2 = RequestHelper.extractFrom(sectionPlayerData, ".card-body h2.card-title");
        String sectionTitle = RequestHelper.extractTextFrom(h2, "*");
        Elements playerInfo = RequestHelper.extractFrom(sectionPlayerData, ".card-body .player-info");

        assertThat(sectionPlayerData).isNotEmpty();

        assertThat(h2).isNotEmpty();
        assertThat(sectionTitle).isEqualTo(expectedCardTitle);

        assertThat(playerInfo).isNotEmpty();
    }

    // @Test
    // @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird das Spieler-Formular korrekt angezeigt.")
    // void test_05() {
    //     String expectedButtonLabel = "Speichern";

    //     Elements playerForm = RequestHelper.extractFrom(playerPage, 
    //         "form#playerForm[method=\"post\"][action=\"/team/savePlayer\"]"
    //     );
    //     Elements labels = RequestHelper.extractFrom(playerForm, "label");
    //     Elements inputs = RequestHelper.extractFrom(playerForm, "input");
    //     String buttonLabel = RequestHelper.extractTextFrom(playerForm, "button[type=\"submit\"]");

    //     assertThat(playerForm).isNotEmpty();
    //     assertThat(labels).isNotEmpty();
    //     assertThat(inputs).isNotEmpty();
    //     assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    // }

    @Test
    @DisplayName("Auf der Seite Spieler bearbeiten/hinzufügen wird das Spieler-Formular korrekt angezeigt.")
    void test_05() {
        String expectedButtonText = "Speichern";
        Elements playerForm = RequestHelper.extractFrom(playerPage, "form#playerForm");
        String method = RequestHelper.extractAttributeFrom(playerForm, "method");
        String action = RequestHelper.extractAttributeFrom(playerForm, "action");
        Elements labels = RequestHelper.extractFrom(playerForm, "label");
        Elements inputs = RequestHelper.extractFrom(playerForm, "input");
        Elements button = RequestHelper.extractFrom(playerForm, "button");
        String type = RequestHelper.extractAttributeFrom(button, "type");
        String buttonText = RequestHelper.extractTextFrom(button, "*");

        assertThat(playerForm).isNotEmpty();
        assertThat(method).isEqualTo("post");
        assertThat(action).isEqualTo("/team/savePlayer");

        assertThat(labels).hasSize(4);
        assertThat(inputs).hasSize(5);

        assertThat(button).isNotEmpty();
        assertThat(type).isEqualTo("submit");
        assertThat(buttonText).isEqualTo(expectedButtonText);
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

    @Test
    @DisplayName("Ein Spieler wird korrekt angezeigt.")
    void test_07() {
        List<String> expectedValues = List.of(
            Integer.toString(player.getId()), 
            player.getFirstName(), player.getLastName(), 
            player.getPosition(), Integer.toString(player.getJerseyNumber())
        );
        Elements formInputs = RequestHelper.extractFrom(playerPage, "form#playerForm input");
        List<String> values = RequestHelper.extractEachAttributeFrom(formInputs, "value");

        assertThat(values).containsExactlyInAnyOrderElementsOf(expectedValues);
    }

    @Test
    @DisplayName("Die Scores eines Spielers werden korrekt angezeigt.")
    void test_08() {
        List<String> expectedElements = scores.entrySet().stream()
                                            .map(e -> e.getKey() + ": " + e.getValue())
                                            .collect(Collectors.toList());
        expectedElements.add("Gesamt-Score: 0.0");

        List<String> playerScores = RequestHelper
            .extractFrom(playerPage, ".card.scores .card-body .player-scores .scores-container p")
            .eachText();

        assertThat(playerScores).containsExactlyInAnyOrderElementsOf(expectedElements);
    }

}
