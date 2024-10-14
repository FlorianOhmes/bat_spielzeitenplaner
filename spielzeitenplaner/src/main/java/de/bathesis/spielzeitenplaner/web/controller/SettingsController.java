package de.bathesis.spielzeitenplaner.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.mapper.FormationMapper;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.web.forms.FormationForm;


@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }


    @GetMapping
    public String settings(Model model) {
        Formation formation = settingsService.loadFormation();
        FormationForm formationForm = FormationMapper.toFormationForm(formation);
        model.addAttribute("formationForm", formationForm);
        return "settings";
    }

    @PostMapping("/saveFormation")
    public String saveFormation(FormationForm formationForm) {
        Formation formation = FormationMapper.toDomainFormation(formationForm);
        settingsService.saveFormation(formation);
        return "redirect:/settings";
    }

}
