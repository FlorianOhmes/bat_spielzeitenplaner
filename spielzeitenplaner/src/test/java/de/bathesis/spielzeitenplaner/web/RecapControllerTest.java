package de.bathesis.spielzeitenplaner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.RecapService;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.utilities.RequestHelper;
import de.bathesis.spielzeitenplaner.web.controller.RecapController;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@WebMvcTest(RecapController.class)
class RecapControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    PlayerService playerService;
    @MockBean
    SettingsService settingsService;
    @MockBean
    RecapService recapService;

    List<Player> players = new ArrayList<>(List.of(
        new Player(1787, "Sandro", "Wagner", "ST", 19), 
        new Player(1981, "Julian", "Nagelsmann", "TW", 1)
    ));
    List<Criterion> criteria = new ArrayList<>(List.of(
        new Criterion(989, "Training", "T", 0.4)
    ));
    List<Assessment> assessments = new ArrayList<>(List.of(
        new Assessment(null, LocalDate.of(2024, 10, 19), criteria.get(0).getId(), players.get(0).getId(), 4.0), 
        new Assessment(null, LocalDate.of(2024, 10, 19), criteria.get(0).getId(), players.get(1).getId(), 2.0)
    ));


    @Test
    @DisplayName("Die Startseite des Recap-Bereichs ist erreichbar.")
    void test_01() throws Exception {
        RequestHelper.performGet(mvc, "/recap")
                     .andExpect(status().isOk())
                     .andExpect(view().name("/recap/start"));
    }

    @Test
    @DisplayName("Die Recap-Seite ist erreichbar.")
    void test_02() throws Exception {
        when(playerService.loadPlayers()).thenReturn(players);
        when(settingsService.loadCriteria()).thenReturn(criteria);

        RequestHelper.performGet(mvc, "/recap/assess?players=1787&players=1981")
                     .andExpect(status().isOk())
                     .andExpect(view().name("/recap/recap"))
                     .andExpect(model().attributeExists("recapForm"))
                     .andExpect(model().attributeExists("numberOfPlayers"))
                     .andExpect(model().attributeExists("numberOfCriteria"));

        verify(playerService).loadPlayers();
        verify(settingsService).loadCriteria();
    }

    @Test
    @DisplayName("Es werden Post-Requests über /recap/assess/submitAssessment akzeptiert.")
    void test_03() throws Exception {
        mvc.perform(postSuccessful())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @DisplayName("Bei Post-Requests über /recap/assess/submitAssessment wird die submitAssessment-Methode des RecapServices aufgerufen.")
    void test_04() throws Exception {
        mvc.perform(postSuccessful());
        verify(recapService).submitAssessments(assessments);
    }

    @Test
    @DisplayName("Das Recap-Formular wird im Controller validiert.")
    void test_05() throws Exception {
        when(playerService.loadPlayers()).thenReturn(players);
        when(settingsService.loadCriteria()).thenReturn(criteria);

        String html = mvc.perform(post("/recap/assess/submitAssessment")
                            .param("assessments[0].playerId", assessments.get(0).getPlayerId().toString())
                            .param("assessments[0].criterionId", assessments.get(0).getCriterionId().toString())
                            .param("assessments[1].playerId", assessments.get(1).getPlayerId().toString())
                            .param("assessments[1].criterionId", assessments.get(1).getCriterionId().toString())
                        )
                        .andExpect(status().isOk())
                        .andExpect(model().attributeErrorCount("recapForm", 3))
                        .andReturn().getResponse().getContentAsString();

        String html2 = mvc.perform(post("/recap/assess/submitAssessment")
                            .param("date", "5034-12-31")
                            .param("assessments[0].playerId", assessments.get(0).getPlayerId().toString())
                            .param("assessments[0].criterionId", assessments.get(0).getCriterionId().toString())
                            .param("assessments[0].value", "-0.2")
                            .param("assessments[1].playerId", assessments.get(1).getPlayerId().toString())
                            .param("assessments[1].criterionId", assessments.get(1).getCriterionId().toString())
                            .param("assessments[1].value", "14.2")
                        )
                        .andExpect(status().isOk())
                        .andExpect(model().attributeErrorCount("recapForm", 3))
                        .andReturn().getResponse().getContentAsString();

        Elements errors = Jsoup.parse(html).select(".error");
        Elements errors2 = Jsoup.parse(html2).select(".error");
        assertThat(errors).hasSize(3);
        assertThat(errors2).hasSize(3);
    }





    private MockHttpServletRequestBuilder postSuccessful() {
        return post("/recap/assess/submitAssessment")
                        .param("date", "2024-10-19")
                        .param("assessments[0].playerId", assessments.get(0).getPlayerId().toString())
                        .param("assessments[0].criterionId", assessments.get(0).getCriterionId().toString())
                        .param("assessments[0].value", assessments.get(0).getValue().toString())
                        .param("assessments[1].playerId", assessments.get(1).getPlayerId().toString())
                        .param("assessments[1].criterionId", assessments.get(1).getCriterionId().toString())
                        .param("assessments[1].value", assessments.get(1).getValue().toString());
    }

}
