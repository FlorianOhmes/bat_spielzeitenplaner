package de.bathesis.spielzeitenplaner.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import de.bathesis.spielzeitenplaner.services.TeamService;


@Controller
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @GetMapping
    public String team() {
        return "team/team";
    }

    @GetMapping("/player")
    public String player() {
        return "team/player";
    }

    @PostMapping("/teamname")
    public String changeTeamName(String teamName) {
        teamService.save(teamName);
        return "redirect:/team";
    }

}
