package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.PlayerDao;
import com.epam.brest.db.h2.SpringJdbcConfig;
import com.epam.brest.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@Import({PlayerDaoJdbc.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlayerDaoJdbcITest {

    @Autowired
    private PlayerDao playerDao;

    @Test
    public void findAllTest() {
        List<Player> players = playerDao.findAll();
        Assertions.assertNotNull(players);
        assertTrue(players.size() > 0);
    }

    @Test
    public void findByIdTest() {
        List<Player> players = playerDao.findAll();
        Assertions.assertNotNull(players);
        assertTrue(players.size() > 0);

        Integer playerId = players.get(0).getPlayerId();
        Player expPlayer = playerDao.findById(playerId).get();
        Assertions.assertEquals(playerId, expPlayer.getPlayerId());
        Assertions.assertEquals(players.get(0).getPlayerId(), expPlayer.getPlayerId());
        Assertions.assertEquals(players.get(0).getFirstName(), expPlayer.getFirstName());
        Assertions.assertEquals(players.get(0).getLastName(), expPlayer.getLastName());
        Assertions.assertEquals(players.get(0).getNationality(), expPlayer.getNationality());
        Assertions.assertEquals(players.get(0).getSalary(), expPlayer.getSalary());
        Assertions.assertEquals(players.get(0).getTeamId(), expPlayer.getTeamId());
        Assertions.assertEquals(players.get(0), expPlayer);
    }

    @Test
    public void findByIdExceptionalTest() {
        Optional<Player> optionalPlayer = playerDao.findById(999);
        assertTrue(optionalPlayer.isEmpty());
    }

    @Test
    public void createPlayerTest() {
        List<Player> players = playerDao.findAll();
        Assertions.assertNotNull(players);
        assertTrue(players.size() > 0);

        playerDao.create(new Player("Miles", "Morales", "American", 12000.0, 1));

        List<Player> realPlayers = playerDao.findAll();
        Assertions.assertEquals(players.size() + 1, realPlayers.size());
    }

    @Test
    public void updatePlayerTest() {
        List<Player> players = playerDao.findAll();
        Assertions.assertNotNull(players);
        assertTrue(players.size() > 0);

        Player player = players.get(0);
        player.setFirstName("TEST_PLAYER");
        playerDao.update(player);

        Optional<Player> realPlayer = playerDao.findById(player.getPlayerId());
        Assertions.assertEquals("TEST_PLAYER", realPlayer.get().getFirstName());
    }

    @Test
    public void deletePlayerTest() {
        List<Player> players = playerDao.findAll();
        Assertions.assertNotNull(players);
        assertTrue(players.size() > 0);

        Integer testId = playerDao.create(new Player("Miles", "Morales", "American", 12000.0, 1));
        playerDao.delete(testId);

        List<Player> realPlayers = playerDao.findAll();
        Assertions.assertEquals(players.size(), realPlayers.size());
    }

}