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
    @DisplayName("Auf der Startseite zur Spielzeitenberechnung wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String pageTitle = RequestHelper.extractFrom(kaderPage, "h1");
        assertThat(pageTitle).isEqualTo(ExpectedElements.SPIELZEITEN_TITLE);
    }

}
