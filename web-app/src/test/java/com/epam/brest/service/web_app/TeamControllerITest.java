package com.epam.brest.service.web_app;

import com.epam.brest.model.Team;
import com.epam.brest.model.dto.TeamDto;
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
class TeamControllerITest {

    private static final Integer TEAM_NAME_SIZE = 50;

    private static final String TEAM_DTOS_URL = "http://localhost:8088/team-dtos";
    private static final String TEAMS_URL = "http://localhost:8088/teams";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    protected ObjectMapper objectMapper;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldReturnTeamsPage() throws Exception {
        Team t1 = createTeam(1, "YOO");
        Team t2 = createTeam(2, "GOO");
        Team t3 = createTeam(3, "FOO");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(t1, t2, t3)))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/teams")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("teams"))
                .andExpect(model().attribute("teams", hasItem(
                        allOf(
                                hasProperty("teamId", is(t1.getTeamId())),
                                hasProperty("teamName", is(t1.getTeamName()))
                        )
                )))
                .andExpect(model().attribute("teams", hasItem(
                        allOf(
                                hasProperty("teamId", is(t2.getTeamId())),
                                hasProperty("teamName", is(t2.getTeamName()))
                        )
                )))
                .andExpect(model().attribute("teams", hasItem(
                        allOf(
                                hasProperty("teamId", is(t3.getTeamId())),
                                hasProperty("teamName", is(t3.getTeamName()))
                        )
                )))
        ;
        mockServer.verify();
    }

    @Test
    public void shouldOpenNewTeamPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("team"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("team", isA(Team.class)));
        mockServer.verify();
    }

    @Test
    public void shouldAddNewTeam() throws Exception {

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("teamName", "test")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams"))
                .andExpect(redirectedUrl("/teams"));

        mockServer.verify();
    }

    @Test
    public void shouldOpenEditTeamPageById() throws Exception {
        Team d = createTeam(1, "COO");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL + "/" + d.getTeamId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(d))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("team"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("team", hasProperty("teamId", is(d.getTeamId()))))
                .andExpect(model().attribute("team", hasProperty("teamName", is(d.getTeamName()))));
        mockServer.verify();
    }

    @Test
    public void shouldReturnToTeamsPageIfTeamNotFoundById() throws Exception {
        int id = 99999;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("teams"));
        mockServer.verify();
    }

    @Test
    public void shouldUpdateTeamAfterEdit() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        String testName = RandomStringUtils.randomAlphabetic(TEAM_NAME_SIZE);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/team/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("teamId", "1")
                        .param("teamName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams"))
                .andExpect(redirectedUrl("/teams"));

        mockServer.verify();
    }

    @Test
    public void shouldDeleteTeam() throws Exception {
        int id = 3;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAM_DTOS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAMS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/3/delete")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams"))
                .andExpect(redirectedUrl("/teams"));
        mockServer.verify();
    }

    private Team createTeam(int id, String name) {
        Team team = new Team();
        team.setTeamId(id);
        team.setTeamName(name);
        return team;
    }
}

