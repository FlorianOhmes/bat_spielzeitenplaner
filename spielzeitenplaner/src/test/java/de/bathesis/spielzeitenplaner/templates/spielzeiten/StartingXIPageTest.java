package de.bathesis.spielzeitenplaner.templates.spielzeiten;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.services.SpielzeitenService;
import de.bathesis.spielzeitenplaner.utilities.ExpectedElements;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import de.bathesis.spielzeitenplaner.web.controller.SpielzeitenController;
import java.util.List;
import java.util.ArrayList;


@WebMvcTest(SpielzeitenController.class)
class StartingXIPageTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SpielzeitenService spielzeitenService;
    @MockBean
    PlayerService playerService;
    @MockBean
    SettingsService settingsService;

    Document startingXIPage;

    List<Player> squad = TestObjectGenerator.generateSquad();


    @BeforeEach
    void getStartingXIPage() throws Exception {
        when(settingsService.loadFormation()).thenReturn(TestObjectGenerator.generateFormation());
        String html = mvc.perform(get("/spielzeiten/startingXI")
                                    .flashAttr("startingXI", squad.subList(0, 11))
                                    .flashAttr("bench", squad.subList(11, 16))
                            )
                            .andReturn().getResponse().getContentAsString();
        startingXIPage = Jsoup.parse(html);
    }


    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird die korrekte Überschrift angezeigt.")
    void test_01() {
        String pageTitle = RequestHelper.extractTextFrom(startingXIPage, "h1");
        assertThat(pageTitle).isEqualTo(ExpectedElements.spielzeitenTitle());
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird die Navigationsleiste angezeigt.")
    void test_02() {
        Elements navbar = RequestHelper.extractFrom(startingXIPage, "nav");
        assertThat(navbar).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird der Footer angezeigt.")
    void test_03() {
        Elements footer = RequestHelper.extractFrom(startingXIPage, "footer");
        assertThat(footer).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird ein Paragraph mit einer kurzen Erklärung angezeigt.")
    void test_04() {
        String leadText = RequestHelper.extractTextFrom(startingXIPage, "p.lead");
        assertThat(leadText).isNotBlank();
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung werden im Bereich Startelf bestätigen die korrekten Titel angezeigt.")
    void test_05() {
        String expectedCardTitle = "Startelf";
        String expectedFormationTitle = "Formation";

        String cardTitle = RequestHelper.extractTextFrom(startingXIPage, "#cardStartingXI .card-body h2.card-title");
        String formationTitle = RequestHelper.extractTextFrom(startingXIPage, "#cardStartingXI .card-body p");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(formationTitle).contains(expectedFormationTitle);
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird im Bereich Startelf bestätigen nach Positionsgruppen unterschieden.")
    void test_06() {
        List<String> expectedPositionGroups = new ArrayList<>(List.of(
            "Angriff", "Mittelfeld", "Abwehr", "Torwart"
        ));

        String positionGroups = RequestHelper.extractTextFrom(startingXIPage, "#cardStartingXI .card-body h3");
        Elements attack = RequestHelper.extractFrom(startingXIPage, "#cardStartingXI .card-body #attack");
        Elements midfield = RequestHelper.extractFrom(startingXIPage, "#cardStartingXI .card-body #midfield");
        Elements defenders = RequestHelper.extractFrom(startingXIPage, "#cardStartingXI .card-body #defenders");
        Elements goalkeeper = RequestHelper.extractFrom(startingXIPage, "#cardStartingXI .card-body #goalkeeper");

        assertThat(positionGroups).contains(expectedPositionGroups);
        assertThat(attack).isNotEmpty();
        assertThat(midfield).isNotEmpty();
        assertThat(defenders).isNotEmpty();
        assertThat(goalkeeper).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird im Bereich Startelf bestätigen ein Formular angezeigt.")
    void test_07() {
        String expectedButtonLabel = "Startelf aktualisieren";

        Elements form = RequestHelper.extractFrom(startingXIPage, 
            "form#startingXI[method=\"post\"][action=\"/spielzeiten/updateStartingXI\"]"
        );
        String formButtonLabel = RequestHelper.extractTextFrom(form, ".form-button button[type=\"submit\"]");

        assertThat(form).isNotEmpty();
        assertThat(formButtonLabel).isEqualTo(expectedButtonLabel);
    }

    @Test
    @DisplayName("Auf der Seite Startelf der Spielzeitenplanung wird im Bereich Startelf bestätigen eine Card für die Reservespieler angezeigt.")
    void test_08() {
        String expectedCardTitle = "Ersatzbank";

        String cardTitle = RequestHelper.extractTextFrom(startingXIPage, "#cardReserve .card-body h2.card-title");
        Elements reserve = RequestHelper.extractFrom(startingXIPage, "#cardReserve .card-body #reserve");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(reserve).isNotEmpty();
    }

    @Test
    @DisplayName("Ein Spieler wird korrekt angezeigt.")
    void test_09() {
        Player player = squad.get(2);
        List<String> expectedValues = new ArrayList<>(List.of(
            player.getFirstName(), player.getLastName(), player.getPosition(), 
            Integer.toString(player.getJerseyNumber()), Double.toString(0.0)
        ));
        String playerCard = RequestHelper.extractFrom(startingXIPage, "#defenders .player")
                                           .text();
        assertThat(playerCard).contains(expectedValues);
    }

}
