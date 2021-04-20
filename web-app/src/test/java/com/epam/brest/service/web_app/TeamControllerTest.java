package com.epam.brest.service.web_app;

import com.epam.brest.model.Team;
import com.epam.brest.service.PlayerService;
import com.epam.brest.service.TeamDtoService;
import com.epam.brest.service.TeamService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    private static final Integer TEAM_NAME_SIZE = 50;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeamService teamService;

    @MockBean
    private TeamDtoService teamDtoService;

    @MockBean
    private PlayerService playerService;

    @Captor
    private ArgumentCaptor<Team> captor;

    @Test
    public void shouldReturnTeamsPage() throws Exception {
        Team t1 = createTeam(1, "GOO");
        Team t2 = createTeam(2, "FOO");
        when(teamService.findAll()).thenReturn(Arrays.asList(t1, t2));
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
        ;
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
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldAddNewTeam() throws Exception {
        String testName = RandomStringUtils.randomAlphabetic(TEAM_NAME_SIZE);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/team")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("teamName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams"))
                .andExpect(redirectedUrl("/teams"));

        verify(teamService).create(captor.capture());

        Team d = captor.getValue();
        assertEquals(testName, d.getTeamName());
    }

    @Test
    public void shouldOpenEditTeamPageById() throws Exception {
        Team d = createTeam(1, "GOO");
        when(teamService.findById(any())).thenReturn(Optional.of(d));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/" + d.getTeamId())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("team"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("team", hasProperty("teamId", is(d.getTeamId()))))
                .andExpect(model().attribute("team", hasProperty("teamName", is(d.getTeamName()))));
    }

    @Test
    public void shouldReturnToTeamsPageIfTeamNotFoundById() throws Exception {
        int id = 99999;
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("teams"));
        verify(teamService).findById(id);
    }

    @Test
    public void shouldUpdateTeamAfterEdit() throws Exception {

        String testName = RandomStringUtils.randomAlphabetic(TEAM_NAME_SIZE);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/team/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("teamId", "1")
                        .param("teamName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams"))
                .andExpect(redirectedUrl("/teams"));

        verify(teamService).update(captor.capture());

        Team d = captor.getValue();
        assertEquals(testName, d.getTeamName());
    }

    @Test
    public void shouldDeleteTeam() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/3/delete")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/teams"))
                .andExpect(redirectedUrl("/teams"));

        verify(teamService).delete(3);
    }

    private Team createTeam(int id, String name) {
        Team team = new Team();
        team.setTeamId(id);
        team.setTeamName(name);
        return team;
    }
}