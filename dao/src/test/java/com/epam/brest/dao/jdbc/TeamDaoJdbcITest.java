package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.TeamDao;
import com.epam.brest.db.h2.SpringJdbcConfig;
import com.epam.brest.model.Team;
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
@Import({TeamDaoJdbc.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamDaoJdbcITest {

    @Autowired
    private TeamDao teamDao;

    @Test
    public void findAllTest() {
        List<Team> teams = teamDao.findAll();
        Assertions.assertNotNull(teams);
        assertTrue(teams.size() > 0);
    }

    @Test
    public void findByIdTest() {
        List<Team> teams = teamDao.findAll();
        Assertions.assertNotNull(teams);
        assertTrue(teams.size() > 0);

        Integer teamId = teams.get(0).getTeamId();
        Team expTeam = teamDao.findById(teamId).get();
        Assertions.assertEquals(teamId, expTeam.getTeamId());
        Assertions.assertEquals(teams.get(0).getTeamName(), expTeam.getTeamName());
        Assertions.assertEquals(teams.get(0), expTeam);
    }

    @Test
    public void findByIdExceptionalTest() {
        Optional<Team> optionalTeam = teamDao.findById(999);
        assertTrue(optionalTeam.isEmpty());
    }

    @Test
    public void createTeamTest() {
        List<Team> teams = teamDao.findAll();
        Assertions.assertNotNull(teams);
        assertTrue(teams.size() > 0);

        teamDao.create(new Team("SPARTAK"));

        List<Team> realTeams = teamDao.findAll();
        Assertions.assertEquals(teams.size() + 1, realTeams.size());
    }

    @Test
    public void createTeamWithSameNameTest() {
        List<Team> teams = teamDao.findAll();
        Assertions.assertNotNull(teams);
        assertTrue(teams.size() > 0);

        Assertions.assertThrows(IllegalArgumentException.class, ()-> {
            teamDao.create(new Team("SPARTAK"));
            teamDao.create(new Team("SPARTAK"));
        });
    }

    @Test
    public void createTeamWithSameNameDiffCaseTest() {
        List<Team> teams = teamDao.findAll();
        Assertions.assertNotNull(teams);
        assertTrue(teams.size() > 0);

        Assertions.assertThrows(IllegalArgumentException.class, ()-> {
            teamDao.create(new Team("SPARTAK"));
            teamDao.create(new Team("Spartak"));
        });
    }

    @Test
    public void updateTeamTest() {
        List<Team> teams = teamDao.findAll();
        Assertions.assertNotNull(teams);
        assertTrue(teams.size() > 0);

        Team team = teams.get(0);
        team.setTeamName("TEST_TEAM");
        teamDao.update(team);

        Optional<Team> realTeam = teamDao.findById(team.getTeamId());
        Assertions.assertEquals("TEST_TEAM", realTeam.get().getTeamName());
    }

    @Test
    public void deleteTeamTest() {
        List<Team> teams = teamDao.findAll();
        Assertions.assertNotNull(teams);
        assertTrue(teams.size() > 0);

        Integer testId = teamDao.create(new Team("SPARTAK"));
        teamDao.delete(testId);

        List<Team> realTeams = teamDao.findAll();
        Assertions.assertEquals(teams.size(), realTeams.size());
    }

}