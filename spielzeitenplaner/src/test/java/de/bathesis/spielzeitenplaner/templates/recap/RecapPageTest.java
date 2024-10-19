package de.bathesis.spielzeitenplaner.templates.recap;

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

import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.RecapController;
import java.util.List;
import java.util.ArrayList;


@WebMvcTest(RecapController.class)
class RecapPageTest {

    @Autowired
    MockMvc mvc;

    Document recapPage;

    @MockBean
    PlayerService playerService;

    @MockBean
    SettingsService settingsService;

    List<Player> players = new ArrayList<>(List.of(
        new Player(1787, "Sandro", "Wagner", "ST", 19), 
        new Player(1199, "Julian", "Nagelsmann", "TW", 1)
    ));
    List<Criterion> criteria = new ArrayList<>(List.of(new Criterion(989, "Training", "T", 0.4)));


    @BeforeEach
    void getRecapPage() throws Exception {
        when(playerService.loadPlayers()).thenReturn(players);
        when(settingsService.loadCriteria()).thenReturn(criteria);
        recapPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/recap/assess?players=1787");
    }


    @Test
    @DisplayName("Auf der Seite Recap wird die korrekte Überschrift angezeigt.")
    void test_01() {
        String expectedTitle = "Recap";
        String pageTitle = RequestHelper.extractTextFrom(recapPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Seite Recap wird die Navigationsleiste angezeigt.")
    void test_02() {
        Elements navbar = RequestHelper.extractFrom(recapPage, "nav");
        assertThat(navbar).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Recap wird der Footer angezeigt.")
    void test_03() {
        Elements footer = RequestHelper.extractFrom(recapPage, "footer");
        assertThat(footer).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Recap wird der Bereich Ansicht wählen korrekt angezeigt.")
    void test_04() {
        String expectedCardTitle = "Ansicht wählen";
        String expectedLabelText = "Wähle die für dich passende Ansicht";
        List<String> expectedOptions = new ArrayList<String>(List.of(
            "Nach Spielern sortiert", "Nach Kriterien sortiert"
        ));

        String cardTitle = RequestHelper.extractTextFrom(recapPage, "#selectView .card-body h2.card-title");
        String labelText = RequestHelper.extractTextFrom(recapPage, "#selectView .card-body form#viewForm label");
        String options = RequestHelper.extractTextFrom(recapPage, 
            "#selectView .card-body form#viewForm select option[value=\"players\"], " + 
            "#selectView .card-body form#viewForm select option[value=\"criteria\"]"
        );

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(labelText).isEqualTo(expectedLabelText);
        assertThat(options).contains(expectedOptions);
    }

    @Test
    @DisplayName("Auf der Seite Recap wird die Ansicht Nach Spielern sortiert korrekt angezeigt.")
    void test_05() {
        String expectedCardTitle = "Spielerbewertung";

        String cardTitle = RequestHelper.extractTextFrom(recapPage, "#playersView .card-body h2.card-title");
        Elements playerRatingForm = RequestHelper.extractFrom(recapPage, "#playersView .card-body form#playerRatingForm");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(playerRatingForm).isNotEmpty();
    }

    @Test
    @DisplayName("Das Formular zur Spielerbewertung der Spieler-Ansicht wird korrekt angezeigt.")
    void test_06() {
        List<String> expectedNavButtonLabels = new ArrayList<String>(List.of(
            "← Vorheriger", "Nächster →"
        ));
        String expectedFormButtonText = "Bewertungen speichern";

        Elements playerContainer = RequestHelper.extractFrom(recapPage, "#playersView .card-body form#playerRatingForm #playerContainer");
        String navButtonLabels = RequestHelper.extractTextFrom(recapPage, "#playersView .card-body form#playerRatingForm .navigation-buttons button");
        String formButtonText = RequestHelper.extractTextFrom(recapPage, "#playersView .card-body form#playerRatingForm .form-button button");

        assertThat(playerContainer).isNotEmpty();
        assertThat(navButtonLabels).contains(expectedNavButtonLabels);
        assertThat(formButtonText).isEqualTo(expectedFormButtonText);
    }

    @Test
    @DisplayName("Auf der Seite Recap wird die Ansicht Nach Kriterien sortiert korrekt angezeigt.")
    void test_07() {
        String expectedCardTitle = "Bewertungen (nach Kriterien sortiert)";

        String cardTitle = RequestHelper.extractTextFrom(recapPage, "#criteriaView h2.card-title");
        Elements criteriaForm = RequestHelper.extractFrom(recapPage, "#criteriaView .card-body #criteriaForm");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(criteriaForm).isNotEmpty();
    }

    @Test
    @DisplayName("Das Formular zur Spielerbewertung der Kriterien-Ansicht wird korrekt angezeigt.")
    void test_08() {
        List<String> expectedValues = new ArrayList<>(List.of(
            criteria.get(0).getId().toString(), players.get(0).getId().toString(), Double.toString(3.0), 
            criteria.get(0).getId().toString(), players.get(1).getId().toString(), Double.toString(0.0)
        ));
        List<String> expectedLabels = new ArrayList<>(players.stream().map(Player::getFirstName).toList());
        String expectedButtonLabel = "Bewertungen speichern";

        Elements form = RequestHelper.extractFrom(recapPage, 
            "form#criteriaForm[method=\"post\"][action=\"/recap/assess/submitAssessment\"]"
        );
        Elements inputs = RequestHelper.extractFrom(form, "input");
        List<String> values = inputs.eachAttr("value");
        List<String> labels = RequestHelper.extractFrom(form, "label").eachText();
        String formButtonText = RequestHelper.extractTextFrom(form, "button[type=\"submit\"]");

        assertThat(inputs).hasSize(7);
        assertThat(values).containsAll(expectedValues);
        assertThat(labels).containsAll(expectedLabels);
        assertThat(formButtonText).isEqualTo(expectedButtonLabel);
    }

}
