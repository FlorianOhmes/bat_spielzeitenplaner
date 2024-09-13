package de.bathesis.spielzeitenplaner.templates;

import de.bathesis.spielzeitenplaner.web.WelcomeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        String html = mvc.perform(get("/"))
                                  .andReturn()
                                  .getResponse()
                                  .getContentAsString();
        Document welcomePage = Jsoup.parse(html);
        String pageTitle = welcomePage.select("h1").text();
        assertThat(pageTitle).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Auf der Startseite wird der Jumbotron korrekt angezeigt.")
    void test_02() throws Exception {
        String expectedJumbotronText = "Der einfachste Weg, deine Jugendfußball-Spiele zu planen und zu verwalten. Nutze unsere Tools, um die Spielzeiten deiner Spieler aufgrund eigens festgelegter Kriterien zu planen.";
        String html = mvc.perform(get("/"))
                                  .andReturn()
                                  .getResponse()
                                  .getContentAsString();
        Document welcomePage = Jsoup.parse(html);
        String jumbotronText = welcomePage.select(".jumbotron p.lead").text();
        assertThat(jumbotronText).isEqualTo(expectedJumbotronText);
    }

}
