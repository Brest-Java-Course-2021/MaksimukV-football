package com.epam.brest.dao;

import com.epam.brest.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamDao {

    List<Team> findAll();

    Optional<Team> findById(Integer teamId);

    Integer create(Team team);

    Integer update(Team team);

    Integer delete(Integer teamId);

    Integer count();

}
