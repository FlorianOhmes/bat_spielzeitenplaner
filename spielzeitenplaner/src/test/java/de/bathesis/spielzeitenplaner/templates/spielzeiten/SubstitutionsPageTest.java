package de.bathesis.spielzeitenplaner.templates.spielzeiten;

import org.jsoup.nodes.Document;
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

}
