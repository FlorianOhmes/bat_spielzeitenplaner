package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WelcomePageController.class)
public class WelcomePageControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die Startseite ist erreichbar.")
    void test_01() throws Exception {
        mvc.perform(get("/"))
           .andExpect(status().isOk());
    }

}
