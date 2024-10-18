package de.bathesis.spielzeitenplaner.templates.recap;

import static org.assertj.core.api.Assertions.assertThat;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.RecapController;


@WebMvcTest(RecapController.class)
public class StartPageTest {

    @Autowired
    MockMvc mvc;

    Document startPage;


    @BeforeEach
    void getStartPage() throws Exception {
        startPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/recap");
    }


    @Test
    @DisplayName("Auf der Startseite wird die korrekte Überschrift angezeigt.")
    void test_01() {
        String expectedTitle = "Recap";
        String pageTitle = RequestHelper.extractTextFrom(startPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Startseite wird die Navigationsleiste angezeigt.")
    void test_02() {
        Elements navbar = RequestHelper.extractFrom(startPage, "nav");
        assertThat(navbar).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Startseite wird der Footer angezeigt.")
    void test_03() {
        Elements footer = RequestHelper.extractFrom(startPage, "footer");
        assertThat(footer).isNotEmpty();
    }

}
