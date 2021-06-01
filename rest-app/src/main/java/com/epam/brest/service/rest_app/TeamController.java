package com.epam.brest.service.rest_app;

import com.epam.brest.model.Team;
import com.epam.brest.service.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@CrossOrigin
@RestController
@Api(tags = {"Team controllers"})
public class TeamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamController.class);

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @ApiOperation(value = "Returns team list")
    @GetMapping(value = "/teams")
    public Collection<Team> teams() {
        return teamService.findAll();
    }

    @ApiOperation(value = "Returns one particular team")
    @GetMapping(value = "/teams/{id}")
    public ResponseEntity<Team> findById(@PathVariable Integer id) {
        LOGGER.debug("findById({})", id);
        Optional<Team> optional = teamService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Creates team instance")
    @PostMapping(value = "/teams", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> createTeam(@RequestBody Team team) {
        LOGGER.debug("createTeam({})", team);
        Integer id = teamService.create(team);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Updates particular team instance")
    @PutMapping(value = "/teams", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> updateTeam(@RequestBody Team team) {
        LOGGER.debug("updateTeam({})", team);
        Integer id = teamService.update(team);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @ApiOperation(value = "Deletes particular teams instance")
    @DeleteMapping(value = "/teams/{id}", produces = {"application/json"})
    public ResponseEntity<Integer> deleteTeam(@PathVariable Integer id) {
        LOGGER.debug("deleteTeam({})", id);
        Integer result = teamService.delete(id);
        return result > 0
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @ApiOperation(value = "Returns teams count")
    @GetMapping(value = "/teams/count")
    public ResponseEntity<Integer> count() {
        return new ResponseEntity<>(teamService.count(), HttpStatus.OK);
    }
}
