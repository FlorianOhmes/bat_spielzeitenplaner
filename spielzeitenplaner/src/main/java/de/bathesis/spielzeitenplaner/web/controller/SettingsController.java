package de.bathesis.spielzeitenplaner.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.mapper.FormationMapper;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.web.forms.FormationForm;
import jakarta.validation.Valid;
import java.util.List;
import java.util.ArrayList;


@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;

    private final List<String> templatePositions = new ArrayList<>(List.of(
            "TW", "LV", "LIV", "RIV", "RV", "LZDM", "RZDM", "LM", "ZOM", "RM", "ST"
        ));

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }


    @GetMapping
    public String settings(Model model) {
        // Befüllung des Models mit Beispielpositionen, die im entsprechenden template jeweils als Placeholder dienen sollen 
        model.addAttribute("templatePositions", templatePositions);

        Formation formation = settingsService.loadFormation();
        FormationForm formationForm = FormationMapper.toFormationForm(formation);
        model.addAttribute("formationForm", formationForm);
        return "settings";
    }

    @PostMapping("/saveFormation")
    public String saveFormation(@Valid FormationForm formationForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Befüllung des Models mit Beispielpositionen, die im entsprechenden template jeweils als Placeholder dienen sollen 
            model.addAttribute("templatePositions", templatePositions);
            return "settings";
        }
        Formation formation = FormationMapper.toDomainFormation(formationForm);
        settingsService.saveFormation(formation);
        return "redirect:/settings";
    }

}
