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
    void test_01() {
        String pageTitle = RequestHelper.extractTextFrom(kaderPage, "h1");
        assertThat(pageTitle).isEqualTo(ExpectedElements.spielzeitenTitle());
    }

    @Test
    @DisplayName("Auf der Seite Kader der Spielzeitenplanung wird die Navigationsleiste korrekt angezeigt.")
    void test_02() {
        String navbarBrandText = RequestHelper.extractTextFrom(kaderPage, "nav.navbar a.navbar-brand");
        String navigationItemsTerms = RequestHelper.extractTextFrom(kaderPage, "nav.navbar ul.navbar-nav li.nav-item");
        assertThat(navbarBrandText).isEqualTo(ExpectedElements.navbrandText());
        assertThat(navigationItemsTerms).contains(ExpectedElements.features());
    }

    @Test
    @DisplayName("Auf der Seite Kader der Spielzeitenplanung wird der Footer korrekt angezeigt.")
    void test_03() {
        String footerText = RequestHelper.extractTextFrom(kaderPage, "footer p");
        assertThat(footerText).isEqualTo(ExpectedElements.footerText());
    }

    @Test
    @DisplayName("Auf der Seite Kader der Spielzeitenplanung wird ein Paragraph mit einer kurzen Erklärung angezeigt.")
    void test_04() {
        String leadText = RequestHelper.extractTextFrom(kaderPage, "p.lead");
        assertThat(leadText).isNotBlank();
    }

    @Test
    @DisplayName("Auf der Seite Kader der Spielzeitenplanung wird der Bereich Kader bestätigen korrekt angezeigt.")
    void test_05() {
        List<String> expectedH2Titles = new ArrayList<>(List.of(
            "Im Kader", "Nicht dabei"
        ));

        Elements form = RequestHelper.extractFrom(kaderPage, "form#kader");
        String h2Titles = RequestHelper.extractTextFrom(form, "h2");
        Elements playersIn = RequestHelper.extractFrom(form, "#playersIn");
        Elements playersOut = RequestHelper.extractFrom(form, "#playersOut");

        assertThat(form).isNotEmpty();
        assertThat(h2Titles).contains(expectedH2Titles);
        assertThat(playersIn).isNotEmpty();
        assertThat(playersOut).isNotEmpty();
    }

}
