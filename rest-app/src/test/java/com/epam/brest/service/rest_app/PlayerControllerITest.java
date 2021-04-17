package com.epam.brest.service.rest_app;

import com.epam.brest.model.Player;
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
class PlayerControllerITest {

    public static final String PLAYERS_ENDPOINT = "/players";

    @Autowired
    private PlayerController playerController;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected MockPlayerService playerService = new MockPlayerService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(playerController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(customExceptionHandler)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldFindAllPlayers() throws Exception {
        List<Player> players = playerService.findAll();
        assertNotNull(players);
        assertTrue(players.size() > 0);
    }

    @Test
    public void shouldFindById() throws Exception {
        List<Player> players = playerService.findAll();
        Assertions.assertNotNull(players);
        assertTrue(players.size() > 0);

        Integer playerId = players.get(0).getPlayerId();
        Player expPlayer = playerService.findById(playerId).get();
        Assertions.assertEquals(playerId, expPlayer.getPlayerId());
        Assertions.assertEquals(players.get(0).getFirstName(), expPlayer.getFirstName());
        Assertions.assertEquals(players.get(0), expPlayer);
    }

    @Test
    public void shouldReturnNotFoundOnMissedPlayer() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(
                PLAYERS_ENDPOINT + "/999999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }

    @Test
    public void shouldCreatePlayer() throws Exception {
        Integer countBefore = playerService.count();

        playerService.create(new Player("Gotch",
                "B***h",
                "Russian",
                10000.0,
                1)
        );

        // verify database size
        Integer countAfter = playerService.count();
        Assertions.assertEquals(countBefore + 1, countAfter);
    }

    @Test
    public void shouldUpdatePlayer() throws Exception {
        List<Player> players = playerService.findAll();
        Assertions.assertNotNull(players);
        assertTrue(players.size() > 0);

        Player player = players.get(0);
        player.setFirstName("TEST_PLAYER");
        playerService.update(player);

        Optional<Player> realPlayer = playerService.findById(player.getPlayerId());
        Assertions.assertEquals("TEST_PLAYER", realPlayer.get().getFirstName());
    }

    @Test
    public void shouldDeletePlayer() throws Exception {
        Integer id = playerService.create(new Player("Gotch",
                "B***h",
                "Russian",
                10000.0,
                1)
        );
        Integer countBefore = playerService.count();

        playerService.delete(id);

        // verify database size
        Integer countAfter = playerService.count();
        Assertions.assertEquals(countBefore - 1, countAfter);
    }

    @Test
    public void shouldReturnNotFoundOnDeleteMissedPlayer() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete(
                PLAYERS_ENDPOINT + "/999999")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);
    }


    /////////////////////////////////////////////////////////////////////////////////

    private class MockPlayerService {

        public List<Player> findAll() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(PLAYERS_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Player>>() {});
        }

        public Optional<Player> findById(Integer playerId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(PLAYERS_ENDPOINT + "/" + playerId)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Player.class));
        }

        public Integer create(Player player) throws Exception {
            String json = objectMapper.writeValueAsString(player);
            MockHttpServletResponse response =
                    mockMvc.perform(post(PLAYERS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isCreated())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer update(Player player) throws Exception {
            MockHttpServletResponse response =
                    mockMvc.perform(put(PLAYERS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(player))
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer delete(Integer playerId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete(new StringBuilder(PLAYERS_ENDPOINT).append("/")
                            .append(playerId).toString())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer count() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(PLAYERS_ENDPOINT + "/count")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);
        }
    }
}