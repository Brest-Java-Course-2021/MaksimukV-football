package com.epam.brest.service.web_app;

import com.epam.brest.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class PlayerControllerITest {

    private static final Integer PLAYER_NAME_SIZE = 50;
    
    private static final String PLAYERS_URL = "http://localhost:8088/players";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldReturnPlayersPage() throws Exception {
        Player p1 = createPlayer(1, "Maxx", "Haxx", "Russian", 12000.0, 1);
        Player p2 = createPlayer(2, "Max", "Hax", "Russia", 1200.0, 2);
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(p1, p2)))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/players")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("players"))
                .andExpect(model().attribute("players", nullValue()))
//                .andExpect(model().attribute("players", hasItem(
//                        allOf(
//                                hasProperty("playerId", is(p1.getPlayerId()))
//                                hasProperty("firstName", is(p1.getFirstName())),
//                                hasProperty("lastName", is(p1.getLastName())),
//                                hasProperty("nationality", is(p1.getNationality())),
//                                hasProperty("salary", is(p1.getSalary())),
//                                hasProperty("teamId", is(p1.getTeamId()))
//                        )
//                )))
//                .andExpect(model().attribute("players", hasItem(
//                        allOf(
//                                hasProperty("playerId", is(p2.getPlayerId())),
//                                hasProperty("firstName", is(p2.getFirstName())),
//                                hasProperty("lastName", is(p2.getLastName())),
//                                hasProperty("nationality", is(p2.getNationality())),
//                                hasProperty("salary", is(p2.getSalary())),
//                                hasProperty("teamId", is(p2.getTeamId()))
//                        )
//                )))
        ;
        mockServer.verify();
    }

    @Test
    public void shouldOpenNewPlayerPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("player"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("player", isA(Player.class)));
        mockServer.verify();
    }

    @Test
    public void shouldAddNewPlayer() throws Exception {

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/player")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "test")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/players"))
                .andExpect(redirectedUrl("/players"));

        mockServer.verify();
    }

    @Test
    public void shouldOpenEditPlayerPageById() throws Exception {
        Player d = createPlayer(1, "Maxx", "Haxx", "Russian", 12000.0, 1);
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL + "/" + d.getPlayerId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(d))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("player"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("player", hasProperty("playerId", is(d.getPlayerId()))))
                .andExpect(model().attribute("player", hasProperty("firstName", is(d.getFirstName()))));
        mockServer.verify();
    }

    @Test
    public void shouldReturnToPlayersPageIfPlayerNotFoundById() throws Exception {
        int id = 99999;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("players"));
        mockServer.verify();
    }

    @Test
    public void shouldUpdatePlayerAfterEdit() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        String testName = RandomStringUtils.randomAlphabetic(PLAYER_NAME_SIZE);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/player/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("playerId", "1")
                        .param("firstName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/players"))
                .andExpect(redirectedUrl("/players"));

        mockServer.verify();
    }

    @Test
    public void shouldDeletePlayer() throws Exception {
        int id = 3;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PLAYERS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player/3/delete")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/players"))
                .andExpect(redirectedUrl("/players"));

        mockServer.verify();
    }

    private Player createPlayer(int id, String firstName, String lastName, String nationality, double salary, int teamId) {
        Player player = new Player();
        player.setPlayerId(id);
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setNationality(nationality);
        player.setSalary(salary);
        player.setPlayerId(teamId);
        return player;
    }
}

