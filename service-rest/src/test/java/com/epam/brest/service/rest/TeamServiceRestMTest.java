package com.epam.brest.service.rest;

import com.epam.brest.model.Team;
import com.epam.brest.service.TeamService;
import com.epam.brest.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.service.rest.config.TestConfig.TEAMS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class TeamServiceRestMTest {

    public static final int TEAM_NAME_SIZE = 50;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TeamService teamService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllTeams() throws Exception {
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createTeam(0),
                                createTeam(1))))
                );

        // when
        List<Team> teams = teamService.findAll();

        // then
        mockServer.verify();
        assertNotNull(teams);
        assertTrue(teams.size() > 0);
    }

    @Test
    public void shouldCreateTeam() throws Exception {
        // given
        Team team = new Team(RandomStringUtils.randomAlphabetic(TEAM_NAME_SIZE));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        Integer id = teamService.create(team);

        // then
        mockServer.verify();
        assertNotNull(id);
    }

    @Test
    public void shouldFindTeamById() throws Exception {

        // given
        Integer id = 1;
        Team team = new Team(RandomStringUtils.randomAlphabetic(TEAM_NAME_SIZE));
        team.setTeamId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(team))
                );

        // when
        Optional<Team> optionalTeam = teamService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalTeam.isPresent());
        assertEquals(optionalTeam.get().getTeamId(), id);
        assertEquals(optionalTeam.get().getTeamName(), team.getTeamName());
    }

    @Test
    public void shouldUpdateTeam() throws Exception {
        // given
        Integer id = 1;
        Team team = new Team(RandomStringUtils.randomAlphabetic(TEAM_NAME_SIZE));
        team.setTeamId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(team))
                );

        // when
        int result = teamService.update(team);
        Optional<Team> updatedTeamOptional = teamService.findById(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);

        assertTrue(updatedTeamOptional.isPresent());
        assertEquals(updatedTeamOptional.get().getTeamId(), id);
        assertEquals(updatedTeamOptional.get().getTeamName(), team.getTeamName());
    }

    @Test
    public void shouldDeleteTeam() throws Exception {
        // given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        int result = teamService.delete(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);
    }

    private Team createTeam(int index) {
        Team team = new Team();
        team.setTeamId(index);
        team.setTeamName("team" + index);
        return team;
    }
}
