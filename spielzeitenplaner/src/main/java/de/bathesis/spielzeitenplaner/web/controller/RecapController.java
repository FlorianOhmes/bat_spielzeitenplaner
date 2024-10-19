package de.bathesis.spielzeitenplaner.web.controller;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import de.bathesis.spielzeitenplaner.domain.Assessment;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.mapper.AssessmentMapper;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.RecapService;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.web.forms.FormAssessment;
import de.bathesis.spielzeitenplaner.web.forms.RecapForm;


@Controller
@RequestMapping("/recap")
public class RecapController {

    private final PlayerService playerService;
    private final SettingsService settingsService;
    private final RecapService recapService;

    public RecapController(PlayerService playerService, SettingsService settingsService, RecapService recapService) {
        this.playerService = playerService;
        this.settingsService = settingsService;
        this.recapService = recapService;
    }


    @GetMapping
    public String start(Model model) {
        List<Player> players = playerService.loadPlayers();
        model.addAttribute("players", players);
        return "/recap/start";
    }

    @GetMapping("/assess")
    public String recap(@RequestParam("players") List<Integer> attendanceIds, Model model) {
        List<Player> allPlayers = playerService.loadPlayers();
        List<Criterion> criteria = settingsService.loadCriteria();

        RecapForm recapForm = generateRecapForm(attendanceIds, allPlayers, criteria);

        model.addAttribute("recapForm", recapForm);
        model.addAttribute("numberOfPlayers", allPlayers.size());
        model.addAttribute("numberOfCriteria", criteria.size());

        return "/recap/recap";
    }

    @PostMapping("/assess/submitAssessment")
    public String submitAssessment(RecapForm recapForm) {
        List<Assessment> assessments = AssessmentMapper.toDomainAssessments(recapForm);
        recapService.submitAssessments(assessments);
        return "redirect:/";
    }





    private RecapForm generateRecapForm(List<Integer> attendanceIds, List<Player> allPlayers, 
                                        List<Criterion> criteria) {
        List<Player> playersPresent = allPlayers.stream()
                                            .filter(p -> attendanceIds.contains(p.getId()))
                                            .toList();
        List<Player> playersAbsent = allPlayers.stream()
                                            .filter(p -> !attendanceIds.contains(p.getId()))
                                            .toList();
        RecapForm recapForm = new RecapForm(LocalDate.now(), new ArrayList<FormAssessment>(List.of()));

        for (Criterion criterion: criteria) {
            for (Player player: playersPresent) {
                FormAssessment assessment = new FormAssessment(
                    criterion.getId(), criterion.getName(), 
                    player.getId(), player.getFirstName(), 3.0
                );
                recapForm.add(assessment);
            }
            for (Player player: playersAbsent) {
                FormAssessment assessment = new FormAssessment(
                    criterion.getId(), criterion.getName(), 
                    player.getId(), player.getFirstName(), 0.0
                );
                recapForm.add(assessment);
            }
        }

        return recapForm;
    }

}
