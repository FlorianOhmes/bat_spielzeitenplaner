package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Position;
import de.bathesis.spielzeitenplaner.mapper.FormationMapper;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import de.bathesis.spielzeitenplaner.web.controller.SettingsController;
import de.bathesis.spielzeitenplaner.web.forms.FormationForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@WebMvcTest(SettingsController.class)
class SettingsControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SettingsService settingsService;

    Formation template = TestObjectGenerator.generateFormation();


    @Test
    @DisplayName("Die Seite Einstellungen ist erreichbar.")
    void test_01() throws Exception {
        when(settingsService.loadFormation()).thenReturn(template);
        RequestHelper.performGet(mvc, "/settings")
                     .andExpect(status().isOk())
                     .andExpect(view().name("settings"));
    }

    @Test
    @DisplayName("Das Model für die Settings-Seite ist korrekt befüllt.")
    void test_02() throws Exception {
        Formation formation = TestObjectGenerator.generateFormation();
        FormationForm formationForm = FormationMapper.toFormationForm(formation);
        when(settingsService.loadFormation()).thenReturn(formation);

        RequestHelper.performGet(mvc, "/settings")
                     .andExpect(model().attribute("formationForm", formationForm));
    }

    @Test
    @DisplayName("Es werden Post-Request über /settings/saveFormation akzeptiert.")
    void test_03() throws Exception {
        mvc.perform(postSuccessful())
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/settings"));
    }

    @Test
    @DisplayName("Bei Post-Request über /settings/saveFormation wird die saveFormation-Methode des SettingsServices aufgerufen.")
    void test_04() throws Exception {
        mvc.perform(postSuccessful());
        verify(settingsService).saveFormation(template);
    }

    @Test
    @DisplayName("Die Formation wird im Controller validiert.")
    void test_05() throws Exception {
        mvc.perform(post("/settings/saveFormation")
                      .param("name", "")
                      .param("positions", 
                            template.getPositions().stream()
                                .map(Position::getName)
                                .collect(Collectors.joining(","))
                      )
                    )
           .andExpect(status().isOk())
           .andExpect(model().attributeErrorCount("formationForm", 1));

        mvc.perform(post("/settings/saveFormation")
                      .param("name", template.getName())
                      .param("positions", 
                            Stream.generate(() -> "").limit(11).collect(Collectors.joining(","))
                      )
                    )
           .andExpect(status().isOk())
           .andExpect(model().attributeErrorCount("formationForm", 11));
    }


    private MockHttpServletRequestBuilder postSuccessful() {
        return post("/settings/saveFormation")
                .param("name", template.getName())
                .param("positions", template.getPositions().stream()
                                            .map(Position::getName)
                                            .collect(Collectors.joining(",")));
    }

}
