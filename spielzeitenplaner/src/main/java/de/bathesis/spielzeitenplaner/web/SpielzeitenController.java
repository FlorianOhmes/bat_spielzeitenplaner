package de.bathesis.spielzeitenplaner.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/spielzeiten")
public class SpielzeitenController {

    @GetMapping
    public String spielzeiten() {
        return "/spielzeiten/start";
    }

    @GetMapping("/kader")
    public String kader() {
        return "/spielzeiten/kader";
    }

    @GetMapping("/startingXI")
    public String startingXI() {
        return "/spielzeiten/startingXI";
    }

}
