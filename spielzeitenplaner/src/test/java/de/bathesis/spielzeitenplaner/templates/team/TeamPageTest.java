package de.bathesis.spielzeitenplaner.templates.team;

import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.TeamService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import de.bathesis.spielzeitenplaner.web.TeamController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


@WebMvcTest(TeamController.class)
class TeamPageTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TeamService teamService;

    @MockBean
    PlayerService playerService;

    Document teamPage;

    private static List<Player> players = TestObjectGenerator.generatePlayers();


    @BeforeEach
    void setUpTeamPage() throws Exception {
        // Generierung der benötigten Test-Objekte
        when(teamService.load()).thenReturn(new Team(77, "Spring Boot FC"));
        when(playerService.loadPlayers()).thenReturn(players);

        // Ausführen des Requests und Bereitstellen der Team-Seite
        teamPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/team");
    }


    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird die korrekte Überschrift angezeigt.")
    void test_01() {
        String expectedTitle = "Team verwalten";
        String pageTitle = RequestHelper.extractTextFrom(teamPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird die Navigationsleiste angezeigt.")
    void test_02() {
        Elements navbar = RequestHelper.extractFrom(teamPage, "nav");
        assertThat(navbar).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird der Footer angezeigt.")
    void test_03() {
        Elements footer = RequestHelper.extractFrom(teamPage, "footer");
        assertThat(footer).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird der Bereich Teamname korrekt angezeigt.")
    void test_04() {
        String expectedcardTitle = "Teamname";
        String expectedButtonLabel = "Speichern";

        String cardTitle = RequestHelper.extractTextFrom(teamPage, ".card.team-name .card-body .card-title");
        Elements teamNameField = RequestHelper.extractFrom(teamPage, "#teamNameField");
        String buttonText = RequestHelper.extractTextFrom(teamPage, "#teamNameBtn");

        assertThat(cardTitle).isEqualTo(expectedcardTitle);
        assertThat(teamNameField).isNotEmpty();
        assertThat(buttonText).isEqualTo(expectedButtonLabel);
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird das Teamname-Formular korrekt angezeigt.")
    void test_05() {
        Elements teamNameForm = RequestHelper.extractFrom(
            teamPage, "form#teamNameForm[method=\"post\"][action=\"/team/teamname\"]"
        );
        Elements teamNameLabel = teamNameForm.select("label");
        Elements teamNameField = teamNameForm.select("input[type=\"text\"][name=\"name\"]");
        Elements button = teamNameForm.select("button[type=\"submit\"]");

        assertThat(teamNameForm).isNotEmpty();
        assertThat(teamNameLabel).isNotEmpty();
        assertThat(teamNameField).isNotEmpty();
        assertThat(button).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird der Bereich Formation korrekt angezeigt.")
    void test_06() {
        String expectedCardTitle = "Formation";
        String expectedButtonLabel = "Formation ändern";
        String expectedButton2Label = "Formationen verwalten";

        String cardTitle = RequestHelper.extractTextFrom(teamPage, ".card.formation .card-title");
        String formationDisplay = RequestHelper.extractTextFrom(teamPage, "#formationDisplay");
        Elements formationField = RequestHelper.extractFrom(teamPage, "#formationField");
        String buttonText = RequestHelper.extractTextFrom(teamPage, "#formationBtn");
        String button2Text = RequestHelper.extractTextFrom(teamPage, "#addFormationBtn");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(formationDisplay).isNotBlank();
        assertThat(formationField).isNotEmpty();
        assertThat(buttonText).isEqualTo(expectedButtonLabel);
        assertThat(button2Text).isEqualTo(expectedButton2Label);
    }

    @Test
    @DisplayName("Auf der Seite zur Teamverwaltung wird der Bereich Spieler im Team korrekt angezeigt.")
    void test_07() {
        String expectedCardTitle = "Spieler im Team";
        String expectedButtonLabel = "Spieler hinzufügen";

        String cardTitle = RequestHelper.extractTextFrom(teamPage, ".card.team h2");
        Elements table = RequestHelper.extractFrom(teamPage, ".card.team .card-body table");
        String buttonLabel = RequestHelper.extractTextFrom(teamPage, ".card.team .card-body a");

        assertThat(cardTitle).isEqualTo(expectedCardTitle);
        assertThat(table).isNotEmpty();
        assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    }

    @Test
    @DisplayName("Die Tabelle mit den Spielern im Team hat die korrekten Überschriften.")
    void test_08() {
        List<String> expectedHeadings = new ArrayList<>(List.of(
            "Name:", "Position:", "Trikotnummer:", "Gesamtscore:", "Aktionen:"
        ));
        String headings = RequestHelper.extractTextFrom(teamPage, ".card.team .card-body table thead tr");
        assertThat(headings).contains(expectedHeadings);
    }

    @Test
    @DisplayName("Die Spieler-Tabelle enthält die korrekten Daten und Elemente.")
    void test_09() {
        List<String> expected = new ArrayList<>(List.of(
            players.get(0).getFirstName(), players.get(0).getLastName(), 
            players.get(0).getPosition(), String.valueOf(players.get(0).getJerseyNumber())
        ));

        Element tableEntry = RequestHelper.extractFrom(teamPage, "table#playerTable tbody tr").first();

        String text = tableEntry.text();
        List<String> playerData = Arrays.asList(text.split(" "));
        Elements deletePlayerForm = tableEntry.select("td form[method=\"post\"][action=\"/team/deletePlayer\"]");
        Elements deleteButton = deletePlayerForm.select("button[type=\"submit\"]");

        assertThat(playerData).containsAll(expected);
        assertThat(deletePlayerForm).isNotEmpty();
        assertThat(deleteButton).isNotEmpty();
    }

}
