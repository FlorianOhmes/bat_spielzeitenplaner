package de.bathesis.spielzeitenplaner.templates;

import de.bathesis.spielzeitenplaner.web.WelcomeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(WelcomeController.class)
public class WelcomePageTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Auf der Startseite wird die korrekte Überschrift angezeigt.")
    void test_01() throws Exception {
        String expectedTitle = "Willkommen beim SpielzeitenPlaner!";
        Document welcomePage = getWelcomePage();
        String pageTitle = extractFrom(welcomePage, "h1");
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Startseite wird der Jumbotron korrekt angezeigt.")
    void test_02() throws Exception {
        String expectedJumbotronText = "Der einfachste Weg, deine Jugendfußball-Spiele zu planen und zu verwalten. Nutze unsere Tools, um die Spielzeiten deiner Spieler aufgrund eigens festgelegter Kriterien zu planen.";
        Document welcomePage = getWelcomePage();
        String jumbotronText = extractFrom(welcomePage, ".jumbotron p.lead");
        assertThat(jumbotronText).isEqualTo(expectedJumbotronText);
    }


    // ausgelagerte Methoden 
    private Document getWelcomePage() throws Exception {
        String html = mvc.perform(get("/"))
                         .andReturn()
                         .getResponse()
                         .getContentAsString();
        Document welcomePage = Jsoup.parse(html);
        return welcomePage;
    }

    private String extractFrom(Document welcomePage, String cssQuery) {
        return welcomePage.select(cssQuery).text();
    }

}
