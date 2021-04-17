package com.epam.brest.service.rest;

import com.epam.brest.model.Player;
import com.epam.brest.service.PlayerService;
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

import static com.epam.brest.service.rest.config.TestConfig.PLAYERS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class PlayerServiceRestMTest {

    public static final int PLAYER_NAME_SIZE = 50;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PlayerService playerService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllPlayers() throws Exception {
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createPlayer(0),
                                createPlayer(1))))
                );

        // when
        List<Player> players = playerService.findAll();

        // then
        mockServer.verify();
        assertNotNull(players);
        assertTrue(players.size() > 0);
    }

    @Test
    public void shouldCreatePlayer() throws Exception {
        // given
        Player player = new Player(RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE),
                RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE),
                RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE),
                Math.random()*10000.0,
                1);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        Integer id = playerService.create(player);

        // then
        mockServer.verify();
        assertNotNull(id);
    }

    @Test
    public void shouldFindPlayerById() throws Exception {

        // given
        Integer id = 1;
        Player player = new Player(RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE),
                RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE),
                RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE),
                Math.random()*10000.0,
                1);
        player.setPlayerId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(player))
                );

        // when
        Optional<Player> optionalPlayer = playerService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalPlayer.isPresent());
        assertEquals(optionalPlayer.get().getPlayerId(), id);
        assertEquals(optionalPlayer.get().getFirstName(), player.getFirstName());
    }

    @Test
    public void shouldUpdatePlayer() throws Exception {
        // given
        Integer id = 1;
        Player player = new Player(RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE),
                RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE),
                RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE),
                Math.random()*10000.0,
                1);
        player.setPlayerId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(player))
                );

        // when
        int result = playerService.update(player);
        Optional<Player> updatedPlayerOptional = playerService.findById(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);

        assertTrue(updatedPlayerOptional.isPresent());
        assertEquals(updatedPlayerOptional.get().getPlayerId(), id);
        assertEquals(updatedPlayerOptional.get().getFirstName(), player.getFirstName());
    }

    @Test
    public void shouldDeletePlayer() throws Exception {
        // given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        int result = playerService.delete(id);

        // then
        mockServer.verify();
        assertTrue(1 == result);
    }

    private Player createPlayer(int index) {
        Player player = new Player();
        player.setPlayerId(index);
        player.setFirstName("fplayer" + index);
        player.setLastName("lplayer" + index);
        player.setNationality("nplayer" + index);
        player.setSalary(2.0 * index);
        player.setTeamId(1);
        return player;
    }
}
