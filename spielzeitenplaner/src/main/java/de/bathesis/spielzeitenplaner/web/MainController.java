package de.bathesis.spielzeitenplaner.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "welcome";
    }

    @GetMapping("/team")
    public String team() {
        return "/team/team";
    }

    @GetMapping("/team/player")
    public String player() {
        return "/team/player";
    }

    @GetMapping("/recap")
    public String recap() {
        return "/recap/recap";
    }

    @GetMapping("/spielzeiten")
    public String spielzeiten() {
        return "/spielzeiten/start.html";
    }

}
