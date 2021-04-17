package com.epam.brest.model;

import org.junit.jupiter.api.*;

public class PlayerUTest {

    @Test
    public void getPlayerSetters() {
        Player player = new Player();

        player.setPlayerId(1);
        player.setFirstName("Maxx");
        player.setLastName("Williams");
        player.setNationality("American");
        player.setSalary(130000.00);
        player.setTeamId(1);

        Assertions.assertEquals(1, player.getPlayerId());
        Assertions.assertEquals("Maxx", player.getFirstName());
        Assertions.assertEquals("Williams", player.getLastName());
        Assertions.assertEquals("American", player.getNationality());
        Assertions.assertEquals(130000.00, player.getSalary());
        Assertions.assertEquals(1, player.getTeamId());
    }
}