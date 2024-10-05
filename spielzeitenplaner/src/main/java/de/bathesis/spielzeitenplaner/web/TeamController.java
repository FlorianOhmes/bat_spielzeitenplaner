package de.bathesis.spielzeitenplaner.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/team")
public class TeamController {

    @GetMapping
    public String team() {
        return "team/team";
    }

    @GetMapping("/player")
    public String player() {
        return "team/player";
    }

    @PostMapping("/teamname")
    public String changeTeamName() {
        return "team/team";
    }

}
