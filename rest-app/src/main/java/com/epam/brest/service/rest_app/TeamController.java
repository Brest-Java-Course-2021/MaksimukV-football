package com.epam.brest.service.rest_app;

import com.epam.brest.model.Team;
import com.epam.brest.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@RestController
public class TeamController {

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping(value = "/teams")
    public Collection<Team> teams() {
        return teamService.findAll();
    }
}
