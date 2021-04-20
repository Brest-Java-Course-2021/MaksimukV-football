package com.epam.brest.service.web_app;

import com.epam.brest.service.TeamDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeamDtoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamDtoController.class);

    private final TeamDtoService teamDtoService;

    @Autowired
    public TeamDtoController(TeamDtoService teamDtoService) {
        this.teamDtoService = teamDtoService;
    }

    @GetMapping(value = "/team-dtos")
    public final String teamDtos(Model model) {
        LOGGER.debug("teamDtos({})", model);
        model.addAttribute("teamDtos", teamDtoService.findAllWithPrefNationality());
        return "teamDtos";
    }
}