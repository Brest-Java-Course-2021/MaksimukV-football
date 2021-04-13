package com.epam.brest.service.rest_app;

import com.epam.brest.model.Team;
import com.epam.brest.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

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
    
    @GetMapping(value = "/teams/{id}")
    public ResponseEntity<Team> findById(@PathVariable Integer id) {
        Optional<Team> optional = teamService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/teams", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> createTeam(@RequestBody Team team) {
        Integer id = teamService.create(team);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping(value = "/teams", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updateTeam(@RequestBody Team team) {
        Integer id = teamService.update(team);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping(value = "/teams/{id}", produces = {"application/json"})
    public ResponseEntity<Integer> deleteTeam(@PathVariable Integer id) {
        Integer result = teamService.delete(id);
        return result > 0
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/teams/count")
    public ResponseEntity<Integer> count() {
        return new ResponseEntity<>(teamService.count(), HttpStatus.OK);
    }
}
