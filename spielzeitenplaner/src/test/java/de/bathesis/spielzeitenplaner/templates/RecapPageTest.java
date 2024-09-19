package de.bathesis.spielzeitenplaner.templates;

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
import de.bathesis.spielzeitenplaner.web.MainController;


@WebMvcTest(MainController.class)
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
        String pageTitle = RequestHelper.extractFrom(recapPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Seite Recap wird die Navigationsleiste korrekt angezeigt.")
    void test_02() throws Exception {
        String navbarBrandText = RequestHelper.extractFrom(recapPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractFrom(recapPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(ExpectedElements.NAVBRAND_TEXT);
        assertThat(navigationItemsTerms).contains(ExpectedElements.FEATURES);
    }

}
