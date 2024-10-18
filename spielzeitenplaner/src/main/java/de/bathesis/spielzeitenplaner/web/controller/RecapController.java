package de.bathesis.spielzeitenplaner.web.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.services.PlayerService;


@Controller
@RequestMapping("/recap")
public class RecapController {

    private final PlayerService playerService;

    public RecapController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @GetMapping
    public String start(Model model) {
        List<Player> players = playerService.loadPlayers();
        model.addAttribute("players", players);
        return "/recap/start";
    }

    @GetMapping("/assess")
    public String recap() {
        return "/recap/recap";
    }

}
