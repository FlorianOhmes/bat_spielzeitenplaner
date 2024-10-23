package de.bathesis.spielzeitenplaner.web.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.services.SpielzeitenService;


@Controller
@RequestMapping("/spielzeiten")
public class SpielzeitenController {

    private final SpielzeitenService spielzeitenService;

    private final PlayerService playerService;
    private final SettingsService settingsService;

    public SpielzeitenController(SpielzeitenService spielzeitenService, PlayerService playerService, 
                                 SettingsService settingsService) {
        this.spielzeitenService = spielzeitenService;
        this.playerService = playerService;
        this.settingsService = settingsService;
    }


    @GetMapping
    public String spielzeiten(Model model) {
        List<Player> players = playerService.loadPlayers();
        model.addAttribute("players", players);
        loadTotalScoresAndAddTo(model, "totalScores", players);
        loadScoreAndAddTo(model, "scoresCriterion1", 0, players);
        loadScoreAndAddTo(model, "scoresCriterion2", 1, players);
        addCriteriaNames(model, "nameCriterion1", "nameCriterion2");
        return "/spielzeiten/start";
    }

    @GetMapping("/kader")
    public String kader(Model model) {
        @SuppressWarnings("unchecked")
        List<Player> squad = (List<Player>) model.getAttribute("squad");
        @SuppressWarnings("unchecked")
        List<Player> notInSquad = (List<Player>) model.getAttribute("notInSquad");

        loadTotalScoresAndAddTo(model, "totalScoresSquad", squad);
        loadTotalScoresAndAddTo(model, "totalScoresNotInSquad", notInSquad);
        loadScoreAndAddTo(model, "scoresCriterion1Squad", 0, squad);
        loadScoreAndAddTo(model, "scoresCriterion1NotInSquad", 0, notInSquad);
        loadScoreAndAddTo(model, "scoresCriterion2Squad", 1, squad);
        loadScoreAndAddTo(model, "scoresCriterion2NotInSquad", 1, notInSquad);
        addCriteriaNames(model, "nameCriterion1", "nameCriterion2");

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

    @PostMapping("/determineKader")
    public String determineKader(@RequestParam(required = false) List<Integer> availablePlayers, 
                                  RedirectAttributes redirectAttributes) {

        if (availablePlayers == null || availablePlayers.size() < 11) {
            redirectAttributes.addFlashAttribute("errorMessage", 
            "Es müssen mindestens 11 Spieler ausgewählt werden!");
            return "redirect:/spielzeiten";
        }

        List<Player> squad = spielzeitenService.determineSquad(availablePlayers);
        List<Player> notInSquad = availablePlayers.stream()
                                        .filter(id -> squad.stream().noneMatch(p -> p.getId().equals(id)))
                                        .map(id -> playerService.loadPlayer(id))
                                        .toList();

        redirectAttributes.addFlashAttribute("squad", squad);
        redirectAttributes.addFlashAttribute("notInSquad", notInSquad);
        return "redirect:/spielzeiten/kader";
    }





    private void loadTotalScoresAndAddTo(Model model, String attributeName, List<Player> players) {
        List<Double> totalScore = players.stream()
                                    .map(player -> playerService.calculateScores(player.getId()))
                                    .map(playerService::calculateTotalScore)
                                    .toList();
        model.addAttribute(attributeName, totalScore);
    }

    private void loadScoreAndAddTo(Model model, String attributeName, Integer index, List<Player> players) {
        List<Double> scores = players.stream()
                                        .map(player -> playerService.calculateScores(player.getId()))
                                        .map(lhm -> {
                                                List<Double> values = lhm.values().stream().toList();
                                                return values.size() > index ? values.get(index) : Double.NaN;
                                        })
                                        .toList();
        model.addAttribute(attributeName, scores);
    }

    private void addCriteriaNames(Model model, String attributeName1, String attributeName2) {
        List<Criterion> criteria = settingsService.loadCriteria();
        if (criteria.size() == 0) {
            model.addAttribute(attributeName1, "");
            model.addAttribute(attributeName2, "");
        } else if (criteria.size() == 1) {
            model.addAttribute(attributeName1, criteria.get(0).getName());
            model.addAttribute(attributeName2, "");
        } else {
            model.addAttribute(attributeName1, criteria.get(0).getName());
            model.addAttribute(attributeName2, criteria.get(1).getName());
        }
    }

}
