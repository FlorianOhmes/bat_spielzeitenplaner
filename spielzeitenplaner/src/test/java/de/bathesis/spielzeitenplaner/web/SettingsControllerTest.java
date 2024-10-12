package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import de.bathesis.spielzeitenplaner.web.controller.SettingsController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


@WebMvcTest(SettingsController.class)
class SettingsControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SettingsService settingsService;


    @Test
    @DisplayName("Die Seite Einstellungen ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/settings").andExpect(status().isOk());
    }

    @Test
    @DisplayName("Es werden Post-Request über /settings/saveFormation akzeptiert.")
    void test_02() throws Exception {
        mvc.perform(post("/settings/saveFormation"))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/settings"));
    }

    @Test
    @DisplayName("Bei Post-Request über /settings/saveFormation wird die saveFormation-Methode des SettingsServices aufgerufen.")
    void test_03() throws Exception {
        Formation formation = TestObjectGenerator.generateFormation();
        mvc.perform(post("/settings/saveFormation")
                .param("name", formation.getName())
                .param("positions", formation.getPositions().stream().collect(Collectors.joining(",")))
        );
        verify(settingsService).save(formation);
    }

}
