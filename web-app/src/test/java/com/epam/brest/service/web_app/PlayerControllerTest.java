package com.epam.brest.service.web_app;

import com.epam.brest.model.Player;
import com.epam.brest.service.PlayerService;
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

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    private static final Integer TEAM_NAME_SIZE = 50;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private TeamService teamService;

    @Captor
    private ArgumentCaptor<Player> captor;

    @Test
    public void shouldReturnPlayersPage() throws Exception {
        Player t1 = createPlayer(1, "GOO", "AAA", "African", 12000.0, 1);
        Player t2 = createPlayer(2, "FOO", "AAA", "African", 12000.0, 1);
        when(playerService.findAll()).thenReturn(Arrays.asList(t1, t2));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/players")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("players"))
                .andExpect(model().attribute("players", hasItem(
                        allOf(
                                hasProperty("playerId", is(t1.getPlayerId())),
                                hasProperty("firstName", is(t1.getFirstName())),
                                hasProperty("lastName", is(t1.getLastName())),
                                hasProperty("nationality", is(t1.getNationality())),
                                hasProperty("salary", is(t1.getSalary())),
                                hasProperty("teamId", is(t1.getTeamId()))
                        )
                )))
                .andExpect(model().attribute("players", hasItem(
                        allOf(
                                hasProperty("playerId", is(t2.getPlayerId())),
                                hasProperty("firstName", is(t2.getFirstName())),
                                hasProperty("lastName", is(t2.getLastName())),
                                hasProperty("nationality", is(t2.getNationality())),
                                hasProperty("salary", is(t2.getSalary())),
                                hasProperty("teamId", is(t2.getTeamId()))
                        )
                )))
        ;
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
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldAddNewPlayer() throws Exception {
        String testName = RandomStringUtils.randomAlphabetic(TEAM_NAME_SIZE);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/player")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", testName)
                        .param("lastName", testName)
                        .param("nationality", testName)
                        .param("salary", "10000.0")
                        .param("teamId", "1")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/players"))
                .andExpect(redirectedUrl("/players"));

        verify(playerService).create(captor.capture());

        Player d = captor.getValue();
        assertEquals(testName, d.getFirstName());
        assertEquals(10000.0, d.getSalary());
    }

    @Test
    public void shouldOpenEditPlayerPageById() throws Exception {
        Player d = createPlayer(1, "GOO", "AAA", "African", 12000.0, 1);
        when(playerService.findById(any())).thenReturn(Optional.of(d));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player/" + d.getPlayerId())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("player"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("player", hasProperty("playerId", is(d.getPlayerId()))))
                .andExpect(model().attribute("player", hasProperty("firstName", is(d.getFirstName()))));
    }

    @Test
    public void shouldReturnToPlayersPageIfPlayerNotFoundById() throws Exception {
        int id = 99999;
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("players"));
        verify(playerService).findById(id);
    }

    @Test
    public void shouldUpdatePlayerAfterEdit() throws Exception {

        String testName = RandomStringUtils.randomAlphabetic(TEAM_NAME_SIZE);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/player/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("playerId", "1")
                        .param("firstName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/players"))
                .andExpect(redirectedUrl("/players"));

        verify(playerService).update(captor.capture());

        Player d = captor.getValue();
        assertEquals(testName, d.getFirstName());
    }

    @Test
    public void shouldDeletePlayer() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/player/3/delete")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/players"))
                .andExpect(redirectedUrl("/players"));

        verify(playerService).delete(3);
    }

    private Player createPlayer(int id, String name, String surname, String nationality, Double salary, int teamId) {
        Player player = new Player();
        player.setPlayerId(id);
        player.setFirstName(name);
        player.setLastName(surname);
        player.setNationality(nationality);
        player.setSalary(salary);
        player.setTeamId(teamId);
        return player;
    }
}