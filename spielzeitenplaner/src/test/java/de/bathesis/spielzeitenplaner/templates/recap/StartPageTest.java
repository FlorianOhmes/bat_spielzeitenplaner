package de.bathesis.spielzeitenplaner.templates.recap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.RecapService;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.RecapController;
import java.util.List;


@WebMvcTest(RecapController.class)
public class StartPageTest {

    @Autowired
    MockMvc mvc;

    Document startPage;

    @MockBean
    PlayerService playerService;
    @MockBean
    SettingsService settingsService;
    @MockBean
    RecapService recapService;

    Player player = new Player(1777, "Hans", "Sarpei", "RV", 2);


    @BeforeEach
    void getStartPage() throws Exception {
        when(playerService.loadPlayers()).thenReturn(List.of(player));
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

    @Test
    @DisplayName("Auf der Startseite wird ein Paragraph mit einer kurzen Erklärung angezeigt.")
    void test_04() {
        String leadText = RequestHelper.extractTextFrom(startPage, "p.lead");
        assertThat(leadText).isNotBlank();
    }

    @Test
    @DisplayName("Das Attendance-Formular wird korrekt angezeigt.")
    void test_05() {
        String expectedLabelText = player.getFirstName() + " " + player.getLastName();

        Elements form = RequestHelper.extractFrom(startPage, "form#attendanceForm[action=\"/recap/assess\"]");
        List<String> inputs = RequestHelper.extractFrom(form, "input[type=\"checkbox\"]").eachAttr("value");
        List<String> labels = RequestHelper.extractFrom(form, "label").eachText();
        Elements button = RequestHelper.extractFrom(form, "button[type=\"submit\"]");

        assertThat(form).isNotEmpty();
        assertThat(inputs).contains(player.getId().toString());
        assertThat(labels).contains(expectedLabelText);
        assertThat(button).isNotEmpty();
    }

}
