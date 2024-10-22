package de.bathesis.spielzeitenplaner.web.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.SettingsService;


@Controller
@RequestMapping("/spielzeiten")
public class SpielzeitenController {

    private final PlayerService playerService;
    private final SettingsService settingsService;

    public SpielzeitenController(PlayerService playerService, SettingsService settingsService) {
        this.playerService = playerService;
        this.settingsService = settingsService;
    }


    @GetMapping
    public String spielzeiten(Model model) {
        List<Player> players = playerService.loadPlayers();
        model.addAttribute("players", players);
        loadAndAddScoresAndData(model, players);
        return "/spielzeiten/start";
    }

    @PostMapping("/kader")
    public String kader() {
        return "/spielzeiten/kader";
    }

    @GetMapping("/startingXI")
    public String startingXI() {
        return "/spielzeiten/startingXI";
    }

    @GetMapping("/substitutions")
    public String substitutions() {
        return "/spielzeiten/substitutions";
    }





    private void loadAndAddScoresAndData(Model model, List<Player> players) {
        List<Double> totalScores = players.stream()
                        .map(player -> playerService.calculateScores(player.getId()))
                        .map(playerService::calculateTotalScore)
                        .toList();
        List<Double> scoresCriterion1 = players.stream()
                        .map(player -> playerService.calculateScores(player.getId()))
                        .map(lhm -> {
                            List<Double> values = lhm.values().stream().toList();
                            return values.size() > 0 ? values.get(0) : Double.NaN;
                        })
                        .toList();
        List<Double> scoresCriterion2 = players.stream()
                        .map(player -> playerService.calculateScores(player.getId()))
                        .map(lhm -> {
                            List<Double> values = lhm.values().stream().toList();
                            return values.size() > 1 ? values.get(1) : Double.NaN;
                        })
                        .toList();

        model.addAttribute("totalScores", totalScores);
        model.addAttribute("scoresCriterion1", scoresCriterion1);
        model.addAttribute("scoresCriterion2", scoresCriterion2);

        List<Criterion> criteria = settingsService.loadCriteria();
        if (criteria.size() == 0) {
            model.addAttribute("nameCriterion1", "");
            model.addAttribute("nameCriterion2", "");
        } else if (criteria.size() == 1) {
            model.addAttribute("nameCriterion1", criteria.get(0).getName());
            model.addAttribute("nameCriterion2", "");
        } else {
            model.addAttribute("nameCriterion1", criteria.get(0).getName());
            model.addAttribute("nameCriterion2", criteria.get(1).getName());
        }
    }

}
