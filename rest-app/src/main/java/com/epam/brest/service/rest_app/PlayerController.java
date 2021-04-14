package com.epam.brest.service.rest_app;

import com.epam.brest.model.Player;
import com.epam.brest.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@CrossOrigin
@RestController
public class PlayerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class);

    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value = "/players")
    public Collection<Player> players() {
        return playerService.findAll();
    }

    @GetMapping(value = "/players/{id}")
    public ResponseEntity<Player> findById(@PathVariable Integer id) {
        LOGGER.debug("findById({})", id);
        Optional<Player> optional = playerService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/players", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> createPlayer(@RequestBody Player player) {
        LOGGER.debug("createPlayer({})", player);
        Integer id = playerService.create(player);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping(value = "/players", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updatePlayer(@RequestBody Player player) {
        LOGGER.debug("updatePlayer({})", player);
        Integer id = playerService.update(player);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping(value = "/players/{id}", produces = {"application/json"})
    public ResponseEntity<Integer> deletePlayer(@PathVariable Integer id) {
        LOGGER.debug("deletePlayer({})", id);
        Integer result = playerService.delete(id);
        return result > 0
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/players/count")
    public ResponseEntity<Integer> count() {
        return new ResponseEntity<>(playerService.count(), HttpStatus.OK);
    }
}
