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

}
