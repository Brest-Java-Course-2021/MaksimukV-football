package com.epam.brest.service.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PlayerController {
    
    @GetMapping(value = "/players")
    public final String players(Model model) {
        return "players";
    }

    @GetMapping(value = "/player/{id}")
    public final String gotoEditPlayerPage(@PathVariable Integer id, Model model) {
        return "player";
    }

    @GetMapping(value = "/player/add")
    public final String gotoAddPlayerPage(Model model) {
        return "player";
    }
}
