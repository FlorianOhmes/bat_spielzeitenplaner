package de.bathesis.spielzeitenplaner.templates;

import de.bathesis.spielzeitenplaner.utilities.ExpectedElements;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.TeamController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.ArrayList;


@WebMvcTest(TeamController.class)
class TeamPageTest {

    @Autowired
    MockMvc mvc;

    Document teamPage;


    @BeforeEach
    void getTeamPage() throws Exception {
        teamPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/team");
    }


    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String expectedTitle = "Team verwalten";
        String pageTitle = RequestHelper.extractTextFrom(teamPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird die Navigationsleiste korrekt angezeigt.")
    void test_02() throws Exception {
        String navbarBrandText = RequestHelper.extractTextFrom(teamPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractTextFrom(teamPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(ExpectedElements.navbrandText());
        assertThat(navigationItemsTerms).contains(ExpectedElements.features());
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird der Footer korrekt angezeigt.")
    void test_03() throws Exception {
        String footerText = RequestHelper.extractTextFrom(teamPage, "footer p");
        assertThat(footerText).isEqualTo(ExpectedElements.footerText());
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird der Bereich Teamname korrekt angezeigt.")
    void test_04() throws Exception {
        String expectedcardTitle = "Teamname";
        String expectedButtonLabel = "Teamnamen ändern";

        String cardTitle = RequestHelper.extractTextFrom(teamPage, ".card.team-name .card-title");
        String teamNameDisplay = RequestHelper.extractTextFrom(teamPage, "#teamNameDisplay");
        Elements teamNameField = teamPage.select("#teamNameField");
        String buttonText = RequestHelper.extractTextFrom(teamPage, "#teamNameBtn");

        assertThat(cardTitle).isEqualTo(expectedcardTitle);
        assertThat(teamNameDisplay).isNotBlank();
        assertThat(teamNameField).isNotEmpty();
        assertThat(buttonText).isEqualTo(expectedButtonLabel);
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird der Bereich Formation korrekt angezeigt.")
    void test_05() throws Exception {
        String expectedCardTitle = "Formation";
        String expectedButtonLabel = "Formation ändern";
        String expectedButton2Label = "Formationen verwalten";

        String cardTitle = RequestHelper.extractTextFrom(teamPage, ".card.formation .card-title");
        String formationDisplay = RequestHelper.extractTextFrom(teamPage, "#formationDisplay");
        Elements formationField = teamPage.select("#formationField");
        String buttonText = RequestHelper.extractTextFrom(teamPage, "#formationBtn");
        String button2Text = RequestHelper.extractTextFrom(teamPage, "#addFormationBtn");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(formationDisplay).isNotBlank();
        assertThat(formationField).isNotEmpty();
        assertThat(buttonText).isEqualTo(expectedButtonLabel);
        assertThat(button2Text).isEqualTo(expectedButton2Label);
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird der Bereich Spieler im Team korrekt angezeigt.")
    void test_06() throws Exception {
        String expectedCardTitle = "Spieler im Team";
        String expectedButtonLabel = "Spieler hinzufügen";

        String cardTitle = RequestHelper.extractTextFrom(teamPage, ".card.team h2");
        Elements table = teamPage.select(".card.team .card-body table");
        String buttonLabel = RequestHelper.extractTextFrom(teamPage, ".card.team .card-body a");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(table).isNotEmpty();
        assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    }

    @Test
    @DisplayName("Die Tabelle mit den Spielern im Team hat die korrekten Überschriften")
    void test_07() throws Exception {
        List<String> expectedHeadings = new ArrayList<>(List.of(
            "Name:", "Pos.:", "Trikotnr.:", "T:", "L:", "S:", "E:", "Ges.:", "Aktionen:"
        ));
        String headings = RequestHelper.extractTextFrom(teamPage, ".card.team .card-body table thead tr");
        assertThat(headings).contains(expectedHeadings);
    }

}
