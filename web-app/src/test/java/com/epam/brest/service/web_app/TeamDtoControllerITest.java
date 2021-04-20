package com.epam.brest.service.web_app;

import com.epam.brest.model.dto.TeamDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
public class TeamDtoControllerITest {

    private static final String TEAM_DTOS_URL = "http://localhost:8088/team-dtos";

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
    public void shouldReturnTeamDtosPage() throws Exception {
        TeamDto t1 = createTeamDto(1, "YOO", "African");
        TeamDto t2 = createTeamDto(2, "GOO", "American");
        TeamDto t3 = createTeamDto(3, "FOO", "FOO");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAM_DTOS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(t1, t2, t3)))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team-dtos")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("teamDtos"))
                .andExpect(model().attribute("teamDtos", hasItem(
                        allOf(
                                hasProperty("teamId", is(t1.getTeamId())),
                                hasProperty("teamName", is(t1.getTeamName())),
                                hasProperty("prefNationality", is(t1.getPrefNationality()))
                        )
                )))
                .andExpect(model().attribute("teamDtos", hasItem(
                        allOf(
                                hasProperty("teamId", is(t2.getTeamId())),
                                hasProperty("teamName", is(t2.getTeamName())),
                                hasProperty("prefNationality", is(t2.getPrefNationality()))
                        )
                )))
                .andExpect(model().attribute("teamDtos", hasItem(
                        allOf(
                                hasProperty("teamId", is(t3.getTeamId())),
                                hasProperty("teamName", is(t3.getTeamName())),
                                hasProperty("prefNationality", is(t3.getPrefNationality()))
                        )
                )))
        ;
        mockServer.verify();
    }

    private TeamDto createTeamDto(int id, String name, String prefNationality) {
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamId(id);
        teamDto.setTeamName(name);
        teamDto.setPrefNationality(prefNationality);
        return teamDto;
    }
}
