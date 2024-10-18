package de.bathesis.spielzeitenplaner.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/recap")
public class RecapController {

    @GetMapping
    public String start() {
        return "/recap/start";
    }

    @GetMapping("/assess")
    public String recap() {
        return "/recap/recap";
    }

}
