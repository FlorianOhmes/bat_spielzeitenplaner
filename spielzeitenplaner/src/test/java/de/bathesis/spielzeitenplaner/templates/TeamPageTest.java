package de.bathesis.spielzeitenplaner.templates;

import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.MainController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(MainController.class)
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
        String pageTitle = RequestHelper.extractFrom(teamPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird die Navigationsleiste korrekt angezeigt.")
    void test_02() throws Exception {
        String expectedNavBrandText = "SpielzeitenPlaner";
        String navbarBrandText = RequestHelper.extractFrom(teamPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractFrom(teamPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(expectedNavBrandText);
        assertThat(navigationItemsTerms).contains(WelcomePageTest.expectedFeatures);
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird der Footer korrekt angezeigt.")
    void test_06() throws Exception {
        String expectedFooterText = "© 2024 SpielzeitenPlaner. Alle Rechte vorbehalten.";
        String footerText = RequestHelper.extractFrom(teamPage, "footer p");
        assertThat(footerText).isEqualTo(expectedFooterText);
    }

}
