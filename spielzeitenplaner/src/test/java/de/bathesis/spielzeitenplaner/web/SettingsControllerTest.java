package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.SettingsController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(SettingsController.class)
class SettingsControllerTest {

    @Autowired
    MockMvc mvc;


    @Test
    @DisplayName("Die Seite Einstellungen ist erreichbar.")
    void test_02() throws Exception {
        RequestHelper.performGet(mvc, "/settings").andExpect(status().isOk());
    }

    @Test
    @DisplayName("Es werden Post-Request über /settings/saveFormation akzeptiert.")
    void test_04() throws Exception {
        mvc.perform(post("/settings/saveFormation"))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/settings"));
    }

}
