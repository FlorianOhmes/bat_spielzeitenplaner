package de.bathesis.spielzeitenplaner.templates;

import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.SettingsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(SettingsController.class)
class SettingsPageTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SettingsService settingsService;

    Document settingsPage;


    @BeforeEach
    void getSettingsPage() throws Exception {
        settingsPage = RequestHelper.performGetAndParseWithJSoup(mvc, "/settings");
    }


    @Test
    @DisplayName("Auf der Seite Einstellungen wird die korrekte Überschrift angezeigt.")
    void test_01() {
        String expectedTitle = "Einstellungen";
        String pageTitle = RequestHelper.extractTextFrom(settingsPage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Seite Einstellungen wird die Navigationsleiste angezeigt.")
    void test_02() {
        Elements navbar = RequestHelper.extractFrom(settingsPage, "nav");
        assertThat(navbar).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Einstellungen wird der Footer angezeigt.")
    void test_03() {
        Elements footer = RequestHelper.extractFrom(settingsPage, "footer");
        assertThat(footer).isNotEmpty();
    }

    @Test
    @DisplayName("Auf der Seite Einstellungen wird die Formation-Card korrekt angezeigt.")
    void test_04() {
        String expectedTitle = "Formation";

        Elements formationCard = RequestHelper.extractFrom(settingsPage, ".card#formationCard");
        String cardTitle = RequestHelper.extractTextFrom(formationCard, ".card-body h2.card-title");
        Elements form = RequestHelper.extractFrom(formationCard, "form#formationForm");

        assertThat(formationCard).isNotEmpty();
        assertThat(cardTitle).isEqualTo(expectedTitle);
        assertThat(form).isNotEmpty();
    }

    @Test
    @DisplayName("Das Formation-Formular der Formation-Card wird korrekt angezeigt.")
    void test_05() {
        String expectedNameLabel = "Bezeichnung:";
        String expectedPositionLabel = "Position 1:";
        String expectedButtonLabel = "Speichern";

        Elements form = RequestHelper.extractFrom(settingsPage, 
            "form#formationForm[method=\"post\"][action=\"/settings/saveFormation\"]");
        String nameLabel = RequestHelper.extractTextFrom(form, "label[for=\"name\"]");
        Elements nameInput = RequestHelper.extractFrom(form, "input[type=\"text\"][name=\"name\"]");
        String positionLabel = RequestHelper.extractTextFrom(form, "label[for=\"position1\"]");
        Elements positionInput = RequestHelper.extractFrom(form, "input[type=\"text\"][name=\"positions[0]\"]");
        String buttonLabel = RequestHelper.extractTextFrom(form, "button[type=\"submit\"]");

        assertThat(nameLabel).isEqualTo(expectedNameLabel);
        assertThat(nameInput).isNotEmpty();
        assertThat(positionLabel).isEqualTo(expectedPositionLabel);
        assertThat(positionInput).isNotEmpty();
        assertThat(buttonLabel).isEqualTo(expectedButtonLabel);
    }

}
