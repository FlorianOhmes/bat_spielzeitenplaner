package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.domain.Position;
import de.bathesis.spielzeitenplaner.domain.Setting;
import de.bathesis.spielzeitenplaner.mapper.CriteriaMapper;
import de.bathesis.spielzeitenplaner.mapper.FormationMapper;
import de.bathesis.spielzeitenplaner.mapper.SettingsMapper;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.utilities.TestObjectGenerator;
import de.bathesis.spielzeitenplaner.web.controller.SettingsController;
import de.bathesis.spielzeitenplaner.web.forms.CriteriaForm;
import de.bathesis.spielzeitenplaner.web.forms.FormCriterion;
import de.bathesis.spielzeitenplaner.web.forms.FormationForm;
import de.bathesis.spielzeitenplaner.web.forms.ScoreSettingsForm;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
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
    List<Setting> settings = new ArrayList<>(List.of(
            new Setting(1195, "weeksGeneral", 6.0), 
            new Setting(1196, "weeksShortTerm", 3.0), 
            new Setting(1197, "weightShortTerm", 0.5), 
            new Setting(1198, "weeksLongTerm", 12.0), 
            new Setting(1199, "weightLongTerm", 0.5)
    ));


    @BeforeEach
    void setUp() {
        when(settingsService.loadFormation()).thenReturn(template);
        when(settingsService.loadCriteria()).thenReturn(criteria);
        when(settingsService.loadScoreSettings()).thenReturn(settings);
    }


    @Test
    @DisplayName("Die Seite Einstellungen ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/settings")
                     .andExpect(status().isOk())
                     .andExpect(view().name("settings"));
    }

    @Test
    @DisplayName("Das Model für die Settings-Seite ist korrekt befüllt.")
    void test_02() throws Exception {
        FormationForm formationForm = FormationMapper.toFormationForm(template);
        CriteriaForm criteriaForm = CriteriaMapper.toCriteriaForm(criteria);
        addEmptyCriterionTo(criteriaForm);
        ScoreSettingsForm scoreSettingsForm = SettingsMapper.toScoreSettingsForm(settings);

        RequestHelper.performGet(mvc, "/settings")
                     .andExpect(model().attribute("formationForm", formationForm))
                     .andExpect(model().attribute("criteriaForm", criteriaForm))
                     .andExpect(model().attribute("scoreSettingsForm", scoreSettingsForm));
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

    @Test
    @DisplayName("Es werden Post-Requests über /settings/saveSettings akzeptiert.")
    void test_10() throws Exception {
        mvc.perform(postSuccessfulScoreSettings())
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/settings"));
    }

    @Test
    @DisplayName("Bei Post-Requests über /settings/saveSettings wird die saveScoreSettings-Methode des SettingsServices aufgerufen.")
    void test_11() throws Exception {
        mvc.perform(postSuccessfulScoreSettings());
        verify(settingsService).saveScoreSettings(settings);
    }

    @Test
    @DisplayName("Die Score-Settings werden im Controller validiert.")
    void test_12() throws Exception {
        String html = mvc.perform(post("/settings/saveSettings"))
                            .andExpect(status().isOk())
                            .andExpect(model().attributeErrorCount("scoreSettingsForm", 5))
                            .andReturn().getResponse().getContentAsString();

        String html2 = mvc.perform(post("/settings/saveSettings")
                                    .param("weeksGeneral", "999")
                                    .param("weeksShortTerm", "0")
                                    .param("weightShortTerm", "3")
                                    .param("weeksLongTerm", "999")
                                    .param("weightLongTerm", "-4")
                                )
                            .andExpect(status().isOk())
                            .andExpect(model().attributeErrorCount("scoreSettingsForm", 5))
                            .andReturn().getResponse().getContentAsString();

        Elements errors = Jsoup.parse(html).select(".error");
        Elements errors2 = Jsoup.parse(html2).select(".error");
        assertThat(errors).hasSize(5);
        assertThat(errors2).hasSize(5);
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

    private MockHttpServletRequestBuilder postSuccessfulScoreSettings() {
        return post("/settings/saveSettings")
                        .param("weeksGeneral", "6")
                        .param("weeksShortTerm", "3")
                        .param("weightShortTerm", "0.5")
                        .param("weeksLongTerm", "12")
                        .param("weightLongTerm", "0.5");
    }

    private void addEmptyCriterionTo(CriteriaForm criteriaForm) {
        List<FormCriterion> newCriteria = new ArrayList<>(criteriaForm.getCriteria());
        newCriteria.add(new FormCriterion());
        criteriaForm.setCriteria(newCriteria);
    }

}
