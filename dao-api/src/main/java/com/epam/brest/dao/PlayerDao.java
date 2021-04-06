package com.epam.brest.dao;

import com.epam.brest.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDao {

    List<Player> findAll();

    Optional<Player> findById(Integer departmentId);

    Integer create(Player department);

    Integer update(Player department);

    Integer delete(Player teamId);

    Integer count();

}
