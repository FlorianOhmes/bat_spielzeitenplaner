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
public class TeamPageTest {

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

}
