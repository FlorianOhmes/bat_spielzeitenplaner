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
class StartingXIPageTest {

    @Autowired
    MockMvc mvc;

    Document startingXIPage;


    @BeforeEach
    void getStartingXIPage() throws Exception {
        startingXIPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/spielzeiten/startingXI");
    }


    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String pageTitle = RequestHelper.extractFrom(startingXIPage, "h1");
        assertThat(pageTitle).isEqualTo(ExpectedElements.spielzeitenTitle());
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird die Navigationsleiste korrekt angezeigt.")
    void test_02() throws Exception {
        String navbarBrandText = RequestHelper.extractFrom(startingXIPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractFrom(startingXIPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(ExpectedElements.navbrandText());
        assertThat(navigationItemsTerms).contains(ExpectedElements.features());
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird der Footer korrekt angezeigt.")
    void test_03() throws Exception {
        String footerText = RequestHelper.extractFrom(startingXIPage, "footer p");
        assertThat(footerText).isEqualTo(ExpectedElements.footerText());
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird ein Paragraph mit einer kurzen Erklärung angezeigt.")
    void test_04() throws Exception {
        String leadText = RequestHelper.extractFrom(startingXIPage, "p.lead");
        assertThat(leadText).isNotBlank();
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung werden im Bereich Startelf bestätigen die korrekten Titel angezeigt.")
    void test_05() throws Exception {
        String expectedCardTitle = "Startelf";
        String expectedFormationTitle = "Formation";

        String cardTitle = RequestHelper.extractFrom(startingXIPage, "#cardStartingXI .card-body h2.card-title");
        String formationTitle = RequestHelper.extractFrom(startingXIPage, "#cardStartingXI .card-body p");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(formationTitle).contains(expectedFormationTitle);
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird im Bereich Startelf bestätigen nach Positionsgruppen unterschieden.")
    void test_06() throws Exception {
        List<String> expectedPositionGroups = new ArrayList<>(List.of(
            "Angriff", "Mittelfeld", "Abwehr", "Torwart"
        ));

        String positionGroups = RequestHelper.extractFrom(startingXIPage, "#cardStartingXI .card-body h3");
        Elements attack = startingXIPage.select("#cardStartingXI .card-body #attack");
        Elements midfield = startingXIPage.select("#cardStartingXI .card-body #midfield");
        Elements defenders = startingXIPage.select("#cardStartingXI .card-body #defenders");
        Elements goalkeeper = startingXIPage.select("#cardStartingXI .card-body #goalkeeper");

        assertThat(positionGroups).contains(expectedPositionGroups);
        assertThat(attack).isNotEmpty();
        assertThat(midfield).isNotEmpty();
        assertThat(defenders).isNotEmpty();
        assertThat(goalkeeper).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird im Bereich Startelf bestätigen ein Formular angezeigt.")
    void test_07() throws Exception {
        String expectedButtonLabel = "Weiter zu \"Wechsel eintragen\"";

        Elements form = startingXIPage.select("form#startingXI");
        String formButtonLabel = RequestHelper.extractFrom(startingXIPage, "form#startingXI .form-button button");

        assertThat(form).isNotEmpty();
        assertThat(formButtonLabel).isEqualTo(expectedButtonLabel);
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird im Bereich Startelf bestätigen eine Card für die Reservespieler angezeigt.")
    void test_08() throws Exception {
        String expectedCardTitle = "Ersatzbank";

        String cardTitle = RequestHelper.extractFrom(startingXIPage, "#cardReserve .card-body h2.card-title");
        Elements reserve = startingXIPage.select("#cardReserve .card-body #reserve");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(reserve).isNotEmpty();
    }

}
