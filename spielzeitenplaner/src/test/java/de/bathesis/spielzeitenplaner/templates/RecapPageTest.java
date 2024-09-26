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
import de.bathesis.spielzeitenplaner.web.RecapController;

import java.util.List;
import java.util.ArrayList;


@WebMvcTest(RecapController.class)
class RecapPageTest {

    @Autowired
    MockMvc mvc;

    Document recapPage;


    @BeforeEach
    void getRecapPage() throws Exception {
        recapPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/recap");
    }


    @Test
    @DisplayName("Auf der Seite Recap wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String expectedTitle = "Recap";
        String pageTitle = RequestHelper.extractTextFrom(recapPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Seite Recap wird die Navigationsleiste korrekt angezeigt.")
    void test_02() throws Exception {
        String navbarBrandText = RequestHelper.extractTextFrom(recapPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractTextFrom(recapPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(ExpectedElements.navbrandText());
        assertThat(navigationItemsTerms).contains(ExpectedElements.features());
    }

    @Test
    @DisplayName("Auf der Seite Recap wird der Footer korrekt angezeigt.")
    void test_03() throws Exception {
        String footerText = RequestHelper.extractTextFrom(recapPage, "footer p");
        assertThat(footerText).isEqualTo(ExpectedElements.footerText());
    }

    @Test
    @DisplayName("Auf der Seite Recap wird der Bereich Ansicht wählen korrekt angezeigt.")
    void test_04() throws Exception {
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
    void test_05() throws Exception {
        String expectedCardTitle = "Spielerbewertung";

        String cardTitle = RequestHelper.extractTextFrom(recapPage, "#playersView .card-body h2.card-title");
        Elements playerRatingForm = recapPage.select("#playersView .card-body form#playerRatingForm");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(playerRatingForm).isNotEmpty();
    }

    @Test
    @DisplayName("Das Formular zur Spielerbewertung der Spieler-Ansicht wird korrekt angezeigt.")
    void test_06() throws Exception {
        List<String> expectedNavButtonLabels = new ArrayList<String>(List.of(
            "← Vorheriger", "Nächster →"
        ));
        String expectedFormButtonText = "Bewertungen speichern";

        Elements playerContainer = recapPage.select("#playersView .card-body form#playerRatingForm #playerContainer");
        String navButtonLabels = RequestHelper.extractTextFrom(recapPage, "#playersView .card-body form#playerRatingForm .navigation-buttons button");
        String formButtonText = RequestHelper.extractTextFrom(recapPage, "#playersView .card-body form#playerRatingForm .form-button button");

        assertThat(playerContainer).isNotEmpty();
        assertThat(navButtonLabels).contains(expectedNavButtonLabels);
        assertThat(formButtonText).isEqualTo(expectedFormButtonText);
    }

    @Test
    @DisplayName("Auf der Seite Recap wird die Ansicht Nach Kriterien sortiert korrekt angezeigt.")
    void test_07() throws Exception {
        String expectedCardTitle = "Kriterium auswählen";

        String cardTitle = RequestHelper.extractTextFrom(recapPage, "#criteriaView h2.card-title");
        Elements criteriaForm = recapPage.select("#criteriaView .card-body #criteriaForm");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(criteriaForm).isNotEmpty();
    }

    @Test
    @DisplayName("Das Formular zur Spielerbewertung der Kriterien-Ansicht wird korrekt angezeigt.")
    void test_08() throws Exception {
        List<String> expectedNavButtonLabels = new ArrayList<String>(List.of(
            "← Vorheriges", "Nächstes →"
        ));
        String expectedFormButtonText = "Bewertungen speichern";

        Elements playerContainer = recapPage.select("#criteriaView .card-body form#criteriaForm #criteriaContainer");
        String navButtonLabels = RequestHelper.extractTextFrom(recapPage, "#criteriaView .card-body form#criteriaForm .navigation-buttons button");
        String formButtonText = RequestHelper.extractTextFrom(recapPage, "#criteriaView .card-body form#criteriaForm .form-button button");

        assertThat(playerContainer).isNotEmpty();
        assertThat(navButtonLabels).contains(expectedNavButtonLabels);
        assertThat(formButtonText).isEqualTo(expectedFormButtonText);
    }

}
