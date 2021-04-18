package com.epam.brest.service.web_app;

import com.epam.brest.model.Player;
import com.epam.brest.service.PlayerService;
import com.epam.brest.service.TeamDtoService;
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
public class PlayerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class);
    
    private final PlayerService playerService;

    private final TeamDtoService teamDtoService;

    @Autowired
    public PlayerController(PlayerService playerService, TeamDtoService teamDtoService) {
        this.playerService = playerService;
        this.teamDtoService = teamDtoService;
    }
    
    @GetMapping(value = "/players")
    public final String players(Model model) {
        LOGGER.debug("players({})", model);
        model.addAttribute("players", playerService.findAll());
        return "players";
    }


    @GetMapping(value = "/player/{id}")
    public final String gotoEditPlayerPage(@PathVariable Integer id, Model model) {
        LOGGER.debug("gotoEditPlayerPage({},{})", id, model);
        Optional<Player> optionalPlayer = playerService.findById(id);
        if (optionalPlayer.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("player", optionalPlayer.get());
            return "player";
        } else {
            return "redirect:players";
        }
    }

    @GetMapping(value = "/player")
    public final String gotoAddPlayerPage(Model model) {
        LOGGER.debug("gotoAddPlayerPage({})", model);
        model.addAttribute("isNew", true);
        model.addAttribute("player", new Player());
        model.addAttribute("teams", teamDtoService.findAllWithPrefNationality());
        return "player";
    }

    @PostMapping(value = "/player")
    public String addPlayer(Player player) {
        LOGGER.debug("addPlayer({})", player);
        this.playerService.create(player);
        return "redirect:/players";
    }

    @PostMapping(value = "/player/{id}")
    public String updatePlayer(Player player) {
        LOGGER.debug("updatePlayer({})", player);
        this.playerService.update(player);
        return "redirect:/players";
    }

    @GetMapping(value = "/player/{id}/delete")
    public final String deletePlayerById(@PathVariable Integer id, Model model) {
        LOGGER.debug("delete({},{})", id, model);
        playerService.delete(id);
        return "redirect:/players";
    }
}
