package com.epam.brest.service;

import com.epam.brest.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    List<Team> findAll();

    Optional<Team> findById(Integer teamId);

    Integer create(Team team);

    Integer update(Team team);

    Integer delete(Integer teamId);

    Integer count();
    
}
