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
class KaderPageTest {

    @Autowired
    MockMvc mvc;

    Document kaderPage;


    @BeforeEach
    void getKaderPage() throws Exception {
        kaderPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/spielzeiten/kader");
    }


    @Test
    @DisplayName("Auf der Seite Kader der Spielzeitenplanung wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String pageTitle = RequestHelper.extractFrom(kaderPage, "h1");
        assertThat(pageTitle).isEqualTo(ExpectedElements.spielzeitenTitle());
    }

    @Test
    @DisplayName("Auf der Seite Kader der Spielzeitenplanung wird die Navigationsleiste korrekt angezeigt.")
    void test_02() throws Exception {
        String navbarBrandText = RequestHelper.extractFrom(kaderPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractFrom(kaderPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(ExpectedElements.navbrandText());
        assertThat(navigationItemsTerms).contains(ExpectedElements.features());
    }

}
