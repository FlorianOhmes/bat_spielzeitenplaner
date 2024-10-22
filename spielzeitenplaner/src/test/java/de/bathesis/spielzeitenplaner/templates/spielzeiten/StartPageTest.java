package de.bathesis.spielzeitenplaner.templates.spielzeiten;

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
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.ExpectedElements;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.SpielzeitenController;
import java.util.ArrayList;
import java.util.List;


@WebMvcTest(SpielzeitenController.class)
class StartPageTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    PlayerService playerService;

    @MockBean
    SettingsService settingsService;

    Document startPage;


    Player player = new Player(42, "Jan", "Oblak", "TW", 13);


    @BeforeEach
    void getStartPage() throws Exception {
        when(playerService.loadPlayers()).thenReturn(List.of(player));
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

        Elements form = RequestHelper.extractFrom(startPage, 
            "form#availablePlayers[method=\"post\"][action=\"/spielzeiten/determineKader\"]"
        );
        String buttonLabel = RequestHelper.extractTextFrom(form, ".form-button button[type=\"submit\"]");

        assertThat(form).isNotEmpty();
        assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    }

    @Test
    @DisplayName("Ein Spieler wird korrekt angezeigt.")
    void test_06() {
        List<String> expectedValues = new ArrayList<>(List.of(
            player.getFirstName(), player.getLastName(), player.getPosition(), 
            Integer.toString(player.getJerseyNumber()), Double.toString(0.0)
        ));
        String playerCard = RequestHelper.extractFrom(startPage, "form#availablePlayers .player-card")
                                           .text();
        assertThat(playerCard).contains(expectedValues);
    }

}
