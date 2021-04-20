package com.epam.brest.service.web_app;

import com.epam.brest.model.Team;
import com.epam.brest.model.dto.TeamDto;
import com.epam.brest.service.PlayerService;
import com.epam.brest.service.TeamDtoService;
import com.epam.brest.service.TeamService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(TeamDtoController.class)
public class TeamDtoControllerTest {
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
        TeamDto t1 = createTeamDto(1, "GOO", "African");
        TeamDto t2 = createTeamDto(2, "FOO", "GOO");
        when(teamDtoService.findAllWithPrefNationality()).thenReturn(Arrays.asList(t1, t2));
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
        ;
    }

    private TeamDto createTeamDto(int id, String name, String prefNationality) {
        TeamDto team = new TeamDto();
        team.setTeamId(id);
        team.setTeamName(name);
        team.setPrefNationality(prefNationality);
        return team;
    }

}
