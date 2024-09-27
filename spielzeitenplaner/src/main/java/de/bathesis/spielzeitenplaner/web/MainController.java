package de.bathesis.spielzeitenplaner.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "welcome";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

}
