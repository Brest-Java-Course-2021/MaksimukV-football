package com.epam.brest.dao;

import com.epam.brest.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDao {

    List<Player> findAll();

    Optional<Player> findById(Integer playerId);

    Integer create(Player player);

    Integer update(Player player);

    Integer delete(Integer playerId);

    Integer count();

}
