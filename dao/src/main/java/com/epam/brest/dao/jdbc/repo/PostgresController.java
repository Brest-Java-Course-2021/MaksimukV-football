package com.epam.brest.dao.jdbc.repo;

import com.epam.brest.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class PostgresController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping(value = "/postgres")
    public Iterable<Player> teams() {
        return playerRepository.findAll();
    }

}
