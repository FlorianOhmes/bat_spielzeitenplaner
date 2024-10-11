package de.bathesis.spielzeitenplaner.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.bathesis.spielzeitenplaner.domain.Player;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.forms.TeamForm;
import de.bathesis.spielzeitenplaner.mapper.TeamMapper;
import de.bathesis.spielzeitenplaner.services.PlayerService;
import de.bathesis.spielzeitenplaner.services.TeamService;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;
    private final PlayerService playerService;

    public TeamController(TeamService teamService, PlayerService playerService) {
        this.teamService = teamService;
        this.playerService = playerService;
    }


    @GetMapping
    public String team(Model model) {
        Team team = teamService.load();
        TeamForm teamForm = TeamMapper.toTeamForm(team);
        model.addAttribute("teamForm", teamForm);

        List<Player> players = playerService.loadPlayers();
        model.addAttribute("players", players);

        return "team/team";
    }

    @GetMapping("/player")
    public String player() {
        return "team/player";
    }

    @PostMapping("/teamname")
    public String changeTeamName(@Valid TeamForm teamForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {return "team/team";}
        Team team = TeamMapper.toDomainTeam(teamForm);
        teamService.save(team);
        redirectAttributes.addFlashAttribute("successMessage", "Teamname erfolgreich gespeichert!");
        return "redirect:/team";
    }

    @PostMapping("/deletePlayer")
    public String deletePlayer(Integer id, RedirectAttributes redirectAttributes) {
        playerService.deletePlayer(id);
        redirectAttributes.addFlashAttribute("successMessage", "Spieler erfolgreich entfernt!");
        return "redirect:/team";
    }

}
