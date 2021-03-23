package com.epam.brest.model;

import org.junit.jupiter.api.*;

public class TeamTest {

    @Test
    public void getTeamNameConstructor() {
        Team team = new Team("Atlanta Falcons");
        Assertions.assertEquals("Atlanta Falcons", team.getTeamName());
    }

    @Test
    public void getTeamNameSetter() {
        Team team = new Team();
        team.setTeamName("Atlanta Falcons");
        Assertions.assertEquals("Atlanta Falcons", team.getTeamName());
    }
}