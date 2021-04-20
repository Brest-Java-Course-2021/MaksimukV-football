package com.epam.brest.service.rest_app;

import com.epam.brest.model.Team;
import com.epam.brest.service.rest_app.exception.CustomExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
class TeamControllerITest {

    public static final String TEAMS_ENDPOINT = "/teams";

    @Autowired
    private TeamController teamController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected MockTeamService teamService = new MockTeamService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(teamController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldFindAllTeams() throws Exception {
        List<Team> teams = teamService.findAll();
        assertNotNull(teams);
        assertTrue(teams.size() > 0);
    }

    @Test
    public void shouldFindById() throws Exception {
        List<Team> teams = teamService.findAll();
        Assertions.assertNotNull(teams);
        assertTrue(teams.size() > 0);

        Integer teamId = teams.get(0).getTeamId();
        Team expTeam = teamService.findById(teamId).get();
        Assertions.assertEquals(teamId, expTeam.getTeamId());
        Assertions.assertEquals(teams.get(0).getTeamName(), expTeam.getTeamName());
        Assertions.assertEquals(teams.get(0), expTeam);
    }

    @Test
    public void shouldReturnNotFoundOnMissedTeam() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(
                TEAMS_ENDPOINT + "/999999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldCreateTeam() throws Exception {
        Integer countBefore = teamService.count();

        teamService.create(new Team("TEAM"));

        // verify database size
        Integer countAfter = teamService.count();
        Assertions.assertEquals(countBefore + 1, countAfter);
    }

    @Test
    public void createTeamWithSameNameTest() throws Exception {
        teamService.create(new Team("TEAM"));

        String json = objectMapper.writeValueAsString(new Team("TEAM"));
        MockHttpServletResponse response =
                mockMvc.perform(post(TEAMS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnprocessableEntity())
                        .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void createTeamWithSameNameDiffCaseTest() throws Exception {
        teamService.create(new Team("TEAM"));

        String json = objectMapper.writeValueAsString(new Team("Team"));
        MockHttpServletResponse response =
                mockMvc.perform(post(TEAMS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnprocessableEntity())
                        .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldUpdateTeam() throws Exception {
        List<Team> teams = teamService.findAll();
        Assertions.assertNotNull(teams);
        assertTrue(teams.size() > 0);

        Team team = teams.get(0);
        team.setTeamName("TEST_TEAM");
        teamService.update(team);

        Optional<Team> realTeam = teamService.findById(team.getTeamId());
        Assertions.assertEquals("TEST_TEAM", realTeam.get().getTeamName());
    }

    @Test
    public void shouldDeleteTeam() throws Exception {
        Integer id = teamService.create(new Team("TEAM"));
        Integer countBefore = teamService.count();

        teamService.delete(id);

        // verify database size
        Integer countAfter = teamService.count();
        Assertions.assertEquals(countBefore - 1, countAfter);
    }

    @Test
    public void shouldReturnNotFoundOnDeleteMissedTeam() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete(
                TEAMS_ENDPOINT + "/999999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }


    /////////////////////////////////////////////////////////////////////////////////

    private class MockTeamService {

        public List<Team> findAll() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(TEAMS_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Team>>() {});
        }

        public Optional<Team> findById(Integer teamId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(TEAMS_ENDPOINT + "/" + teamId)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Team.class));
        }

        public Integer create(Team team) throws Exception {
            String json = objectMapper.writeValueAsString(team);
            MockHttpServletResponse response =
                    mockMvc.perform(post(TEAMS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isCreated())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer update(Team team) throws Exception {
            MockHttpServletResponse response =
                    mockMvc.perform(put(TEAMS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(team))
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer delete(Integer teamId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete(new StringBuilder(TEAMS_ENDPOINT).append("/")
                            .append(teamId).toString())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer count() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(TEAMS_ENDPOINT + "/count")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
    }
}