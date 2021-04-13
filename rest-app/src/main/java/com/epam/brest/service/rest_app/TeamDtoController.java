package com.epam.brest.service.rest_app;

import com.epam.brest.model.dto.TeamDto;
import com.epam.brest.service.TeamDtoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@CrossOrigin
@RestController
public class TeamDtoController {

    private TeamDtoService teamDtoService;

    public TeamDtoController(TeamDtoService teamDtoService) {
        this.teamDtoService = teamDtoService;
    }

    @GetMapping(value = "/team-dtos")
    public Collection<TeamDto> findAllWithPrefNationality() {
        return teamDtoService.findAllWithPrefNationality();
    }

}
