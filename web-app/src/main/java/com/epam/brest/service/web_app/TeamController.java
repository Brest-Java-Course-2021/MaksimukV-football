package com.epam.brest.service.web_app;

import com.epam.brest.model.Team;
import com.epam.brest.service.TeamDtoService;
import com.epam.brest.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class TeamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamController.class);

    private final TeamDtoService teamDtoService;

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamDtoService teamDtoService,
                          TeamService teamService) {
        this.teamDtoService = teamDtoService;
        this.teamService = teamService;
    }

    @GetMapping(value = "/teams")
    public final String teams(Model model) {
        LOGGER.debug("teams({})", model);
        model.addAttribute("teams", teamDtoService.findAllWithPrefNationality());
        return "teams";
    }

    @GetMapping(value = "/team/{id}")
    public final String gotoEditTeamPage(@PathVariable Integer id, Model model) {
        LOGGER.debug("gotoEditTeamPage({},{})", id, model);
        Optional<Team> optionalTeam = teamService.findById(id);
        if (optionalTeam.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("team", optionalTeam.get());
            return "team";
        } else {
            return "redirect:teams";
        }
    }

    @GetMapping(value = "/team")
    public final String gotoAddTeamPage(Model model) {
        LOGGER.debug("gotoAddTeamPage({})", model);
        model.addAttribute("isNew", true);
        model.addAttribute("team", new Team());
        return "team";
    }

    @PostMapping(value = "/team")
    public String addTeam(Team team) {
        LOGGER.debug("addTeam({})", team);
        this.teamService.create(team);
        return "redirect:/teams";
    }

    @PostMapping(value = "/team/{id}")
    public String updateTeam(Team team) {
        LOGGER.debug("updateTeam({})", team);
        this.teamService.update(team);
        return "redirect:/teams";
    }

    @GetMapping(value = "/team/{id}/delete")
    public final String deleteTeamById(@PathVariable Integer id, Model model) {
        LOGGER.debug("delete({},{})", id, model);
        teamService.delete(id);
        return "redirect:/teams";
    }
}
