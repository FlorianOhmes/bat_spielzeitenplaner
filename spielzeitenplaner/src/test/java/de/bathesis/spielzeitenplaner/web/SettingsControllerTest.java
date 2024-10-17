package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Position;
import de.bathesis.spielzeitenplaner.mapper.CriteriaMapper;
import de.bathesis.spielzeitenplaner.mapper.FormationMapper;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import de.bathesis.spielzeitenplaner.web.controller.SettingsController;
import de.bathesis.spielzeitenplaner.web.forms.CriteriaForm;
import de.bathesis.spielzeitenplaner.web.forms.FormCriterion;
import de.bathesis.spielzeitenplaner.web.forms.FormationForm;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;
import de.bathesis.spielzeitenplaner.domain.Criterion;


@WebMvcTest(SettingsController.class)
class SettingsControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SettingsService settingsService;

    Formation template = TestObjectGenerator.generateFormation();

    List<Criterion> criteria = new ArrayList<>(List.of(
            TestObjectGenerator.generateCriteria().get(0), TestObjectGenerator.generateCriteria().get(1)
    ));


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

        List<Criterion> criteria = TestObjectGenerator.generateCriteria();
        CriteriaForm criteriaForm = CriteriaMapper.toCriteriaForm(criteria);
        addEmptyCriterionTo(criteriaForm);
        when(settingsService.loadCriteria()).thenReturn(criteria);

        RequestHelper.performGet(mvc, "/settings")
                     .andExpect(model().attribute("formationForm", formationForm))
                     .andExpect(model().attribute("criteriaForm", criteriaForm));
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

    @Test
    @DisplayName("Es werden Post-Requests über /settings/saveCriteria akzeptiert.")
    void test_06() throws Exception {
        mvc.perform(postSuccessfulCriteria())
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/settings"));
    }

    @Test
    @DisplayName("Bei Post-Requests über /settings/saveCriteria wird die updateCriteria-Methode des SettingsServices aufgerufen.")
    void test_07() throws Exception {
        mvc.perform(postSuccessfulCriteria());
        verify(settingsService).updateCriteria(criteria);
    }

    @Test
    @DisplayName("Die Kriterien werden im Controller validiert.")
    void test_08() throws Exception {
        when(settingsService.loadFormation()).thenReturn(template);

        String html = mvc.perform(post("/settings/saveCriteria")
                                    .param("criteria[0].name", "")
                                    .param("criteria[0].abbrev", "")
                                    )
           .andExpect(status().isOk())
           .andExpect(model().attributeErrorCount("criteriaForm", 4))
           .andReturn().getResponse().getContentAsString();

        Elements errors = Jsoup.parse(html).select(".error");
        assertThat(errors).hasSize(3);
    }

    @Test
    @DisplayName("Zum Löschen markierte Kriterien werden mit der deleteCriteria-Methode des Settings-Service aufgerufen.")
    void test_09() throws Exception {
        Criterion criterion = new Criterion(1333, "Training", "T", 0.4);
        Criterion criterion2 = new Criterion(1544, "Leistung", "L", 0.4);
        List<Criterion> toDelete = new ArrayList<>(List.of(criterion2));

        mvc.perform(post("/settings/saveCriteria")
                      .param("criteria[0].id", criterion.getId().toString()).param("criteria[0].name", criterion.getName())
                      .param("criteria[0].abbrev", criterion.getAbbrev()).param("criteria[0].weight", criterion.getWeight().toString())
                      .param("criteria[1].id", criterion2.getId().toString()).param("criteria[1].name", criterion2.getName())
                      .param("criteria[1].abbrev", criterion2.getAbbrev()).param("criteria[1].weight", criterion2.getWeight().toString())
                      .param("criteria[1].toDelete", "true")

        );

        verify(settingsService).deleteCriteria(toDelete);
    }





    private MockHttpServletRequestBuilder postSuccessfulCriteria() {
        return post("/settings/saveCriteria")
                      .param("criteria[0].id", criteria.get(0).getId().toString())
                      .param("criteria[0].name", criteria.get(0).getName())
                      .param("criteria[0].abbrev", criteria.get(0).getAbbrev())
                      .param("criteria[0].weight", criteria.get(0).getWeight().toString())
                      .param("criteria[1].id", criteria.get(1).getId().toString())
                      .param("criteria[1].name", criteria.get(1).getName())
                      .param("criteria[1].abbrev", criteria.get(1).getAbbrev())
                      .param("criteria[1].weight", criteria.get(1).getWeight().toString());
    }

    private MockHttpServletRequestBuilder postSuccessful() {
        return post("/settings/saveFormation")
                .param("name", template.getName())
                .param("positions", template.getPositions().stream()
                                            .map(Position::getName)
                                            .collect(Collectors.joining(",")));
    }

    private void addEmptyCriterionTo(CriteriaForm criteriaForm) {
        List<FormCriterion> newCriteria = new ArrayList<>(criteriaForm.getCriteria());
        newCriteria.add(new FormCriterion());
        criteriaForm.setCriteria(newCriteria);
    }

}
