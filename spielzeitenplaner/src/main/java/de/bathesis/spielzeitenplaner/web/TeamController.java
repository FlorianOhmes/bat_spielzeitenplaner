package de.bathesis.spielzeitenplaner.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import de.bathesis.spielzeitenplaner.domain.Team;
import de.bathesis.spielzeitenplaner.forms.TeamForm;
import de.bathesis.spielzeitenplaner.mapper.TeamMapper;
import de.bathesis.spielzeitenplaner.services.TeamService;


@Controller
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @GetMapping
    public String team(Model model) {
        Team team = teamService.load();
        TeamForm teamForm = TeamMapper.toTeamForm(team);
        model.addAttribute("teamForm", teamForm);
        return "team/team";
    }

    @GetMapping("/player")
    public String player() {
        return "team/player";
    }

    @PostMapping("/teamname")
    public String changeTeamName(@ModelAttribute TeamForm teamForm) {
        Team team = TeamMapper.toDomainTeam(teamForm);
        teamService.save(team);
        return "redirect:/team";
    }

}
