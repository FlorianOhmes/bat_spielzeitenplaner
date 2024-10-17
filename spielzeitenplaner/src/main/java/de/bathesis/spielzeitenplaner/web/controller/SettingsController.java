package de.bathesis.spielzeitenplaner.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.bathesis.spielzeitenplaner.domain.Criterion;
import de.bathesis.spielzeitenplaner.domain.Formation;
import de.bathesis.spielzeitenplaner.mapper.CriteriaMapper;
import de.bathesis.spielzeitenplaner.mapper.CriterionMapper;
import de.bathesis.spielzeitenplaner.mapper.FormationMapper;
import de.bathesis.spielzeitenplaner.services.SettingsService;
import de.bathesis.spielzeitenplaner.web.forms.CriteriaForm;
import de.bathesis.spielzeitenplaner.web.forms.FormCriterion;
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
        loadAndAddCriteria(model);
        loadAndAddFormation(model);
        return "settings";
    }

    @PostMapping("/saveFormation")
    public String saveFormation(@Valid FormationForm formationForm, BindingResult bindingResult, 
                                Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Befüllung des Models mit den nötigen Attributen 
            model.addAttribute("templatePositions", templatePositions);
            loadAndAddCriteria(model);
            return "settings";
        }
        Formation formation = FormationMapper.toDomainFormation(formationForm);
        settingsService.saveFormation(formation);
        redirectAttributes.addFlashAttribute("successMessage", "Formation erfolgreich gespeichert!");
        return "redirect:/settings";
    }

    @PostMapping("/saveCriteria")
    public String saveCriteria(@Valid CriteriaForm criteriaForm, BindingResult bindingResult, 
                                Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Befüllung des Models mit den nötigen Attributen 
            model.addAttribute("templatePositions", templatePositions);
            loadAndAddFormation(model);
            return "settings";
        }

        // Kriterien aufteilen in zu löschende und zu speichernde Kriterien
        List<Criterion> toDelete = criteriaForm.getCriteria().stream()
                                        .filter(FormCriterion::isToDelete)
                                        .map(CriterionMapper::toDomainCriterion)
                                        .toList();
        List<Criterion> toSave = criteriaForm.getCriteria().stream()
                                        .filter(c -> !c.isToDelete())
                                        .map(CriterionMapper::toDomainCriterion)
                                        .toList();

        settingsService.deleteCriteria(toDelete);
        settingsService.updateCriteria(toSave);

        redirectAttributes.addFlashAttribute("successMessage", "Kriterien erfolgreich gespeichert!");
        return "redirect:/settings";
    }


    private void loadAndAddCriteria(Model model) {
        List<Criterion> criteria = new ArrayList<>(settingsService.loadCriteria());
        // Leeres Kriterium hinzufüen, um die Eingabe eines neuen Kriteriums zu ermöglichen 
        criteria.add(new Criterion(null, null, null, null));
        CriteriaForm criteriaForm = CriteriaMapper.toCriteriaForm(criteria);
        model.addAttribute("criteriaForm", criteriaForm);
    }

    private void loadAndAddFormation(Model model) {
        Formation formation = settingsService.loadFormation();
        FormationForm formationForm = FormationMapper.toFormationForm(formation);
        model.addAttribute("formationForm", formationForm);
    }

}
