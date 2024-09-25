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
import de.bathesis.spielzeitenplaner.web.SpielzeitenController;
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
    void test_01() throws Exception {
        String pageTitle = RequestHelper.extractFrom(substitutionsPage, "h1");
        assertThat(pageTitle).isEqualTo(ExpectedElements.spielzeitenTitle());
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird die Navigationsleiste korrekt angezeigt.")
    void test_02() throws Exception {
        String navbarBrandText = RequestHelper.extractFrom(substitutionsPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractFrom(substitutionsPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(ExpectedElements.navbrandText());
        assertThat(navigationItemsTerms).contains(ExpectedElements.features());
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird der Footer korrekt angezeigt.")
    void test_03() throws Exception {
        String footerText = RequestHelper.extractFrom(substitutionsPage, "footer p");
        assertThat(footerText).isEqualTo(ExpectedElements.footerText());
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird ein Paragraph mit einer kurzen Erklärung angezeigt.")
    void test_04() throws Exception {
        String leadText = RequestHelper.extractFrom(substitutionsPage, "p.lead");
        assertThat(leadText).isNotBlank();
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung werden die Titel der Startelf-Card korrekt angezeigt.")
    void test_05() throws Exception {
        String expectedCardTitle = "Startelf";
        String expectedFormationTitle = "Formation";

        String cardTitle = RequestHelper.extractFrom(substitutionsPage, "#cardStartingXI .card-body h2.card-title");
        String formationTitle = RequestHelper.extractFrom(substitutionsPage, "#cardStartingXI .card-body p");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(formationTitle).contains(expectedFormationTitle);
    }

    @Test
    @DisplayName("Auf der Seite Wechsel eintragen der Spielzeitenplanung wird in der Startelf-Card nach Positionsgruppen unterschieden.")
    void test_06() throws Exception {
        List<String> expectedPositionGroups = new ArrayList<>(List.of(
            "Angriff", "Mittelfeld", "Abwehr", "Torwart"
        ));

        String positionGroups = RequestHelper.extractFrom(substitutionsPage, "#cardStartingXI .card-body h3");
        Elements attack = substitutionsPage.select("#cardStartingXI .card-body #attack");
        Elements midfield = substitutionsPage.select("#cardStartingXI .card-body #midfield");
        Elements defenders = substitutionsPage.select("#cardStartingXI .card-body #defenders");
        Elements goalkeeper = substitutionsPage.select("#cardStartingXI .card-body #goalkeeper");

        assertThat(positionGroups).contains(expectedPositionGroups);
        assertThat(attack).isNotEmpty();
        assertThat(midfield).isNotEmpty();
        assertThat(defenders).isNotEmpty();
        assertThat(goalkeeper).isNotEmpty();
    }

}
