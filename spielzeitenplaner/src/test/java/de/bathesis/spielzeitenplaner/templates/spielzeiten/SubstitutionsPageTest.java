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
import de.bathesis.spielzeitenplaner.web.controller.SpielzeitenController;
import java.util.List;
import java.util.ArrayList;


@WebMvcTest(SpielzeitenController.class)
class SubstitutionsPageTest {

    @Autowired
    MockMvc mvc;

    Document substitutionsPage;


    @BeforeEach
    void getSubstitutionsPage() throws Exception {
        substitutionsPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/spielzeiten/substitutions");
    }


    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird die korrekte Überschrift angezeigt.")
    void test_01() {
        String pageTitle = RequestHelper.extractTextFrom(substitutionsPage, "h1");
        assertThat(pageTitle).isEqualTo(ExpectedElements.spielzeitenTitle());
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird die Navigationsleiste angezeigt.")
    void test_02() {
        Elements navbar = RequestHelper.extractFrom(substitutionsPage, "nav");
        assertThat(navbar).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird der Footer angezeigt.")
    void test_03() {
        Elements footer = RequestHelper.extractFrom(substitutionsPage, "footer");
        assertThat(footer).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird ein Paragraph mit einer kurzen Erklärung angezeigt.")
    void test_04() {
        String leadText = RequestHelper.extractTextFrom(substitutionsPage, "p.lead");
        assertThat(leadText).isNotBlank();
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung werden die Titel der Startelf-Card korrekt angezeigt.")
    void test_05() {
        String expectedCardTitle = "Startelf";
        String expectedFormationTitle = "Formation";

        String cardTitle = RequestHelper.extractTextFrom(substitutionsPage, "#cardStartingXI .card-body h2.card-title");
        String formationTitle = RequestHelper.extractTextFrom(substitutionsPage, "#cardStartingXI .card-body p");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(formationTitle).contains(expectedFormationTitle);
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird in der Startelf-Card nach Positionsgruppen unterschieden.")
    void test_06() {
        List<String> expectedPositionGroups = new ArrayList<>(List.of(
            "Angriff", "Mittelfeld", "Abwehr", "Torwart"
        ));

        String positionGroups = RequestHelper.extractTextFrom(substitutionsPage, "#cardStartingXI .card-body h3");
        Elements attack = RequestHelper.extractFrom(substitutionsPage, "#cardStartingXI .card-body #attack");
        Elements midfield = RequestHelper.extractFrom(substitutionsPage, "#cardStartingXI .card-body #midfield");
        Elements defenders = RequestHelper.extractFrom(substitutionsPage, "#cardStartingXI .card-body #defenders");
        Elements goalkeeper = RequestHelper.extractFrom(substitutionsPage, "#cardStartingXI .card-body #goalkeeper");

        assertThat(positionGroups).contains(expectedPositionGroups);
        assertThat(attack).isNotEmpty();
        assertThat(midfield).isNotEmpty();
        assertThat(defenders).isNotEmpty();
        assertThat(goalkeeper).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird die Ersatzbank-Card korrekt angezeigt.")
    void test_07() {
        String expectedCardTitle = "Ersatzbank";

        String cardTitle = RequestHelper.extractTextFrom(substitutionsPage, "#cardReserve .card-body h2.card-title");
        Elements reserve = RequestHelper.extractFrom(substitutionsPage, "#cardReserve .card-body #reserve");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(reserve).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird die Wechsel-Card korrekt angezeigt.")
    void test_08() {
        String expectedCardTitle = "Wechsel";

        String cardTitle = RequestHelper.extractTextFrom(substitutionsPage, "#cardSubstitutions .card-body h2.card-title");
        Elements substitutionsList = RequestHelper.extractFrom(substitutionsPage, "#cardSubstitutions .card-body ul");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(substitutionsList).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird ein Formular für die bereits eingetragenen Wechsel angezeigt.")
    void test_09() {
        Elements form = RequestHelper.extractFrom(substitutionsPage, "form#formSubstitutions");
        assertThat(form).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird das Formular zum Eintragen eines neuen Wechsels korrekt angezeigt.")
    void test_10() {
        String expectedButtonLabel = "Wechsel eintragen";

        Elements form = RequestHelper.extractFrom(substitutionsPage, "form#addSubstitution");
        Elements playerIn = RequestHelper.extractFrom(form, "select#playerIn");
        Elements playerOut = RequestHelper.extractFrom(form, "select#playerOut");
        String buttonLabel = RequestHelper.extractTextFrom(form, "button");

        assertThat(form).isNotEmpty();
        assertThat(playerIn).isNotEmpty();
        assertThat(playerOut).isNotEmpty();
        assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    }

}
