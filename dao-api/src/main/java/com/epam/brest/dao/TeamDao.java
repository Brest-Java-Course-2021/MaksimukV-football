package com.epam.brest.dao;

import com.epam.brest.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamDao {

    List<Team> findAll();

    Optional<Team> findById(Integer departmentId);

    Integer create(Team department);

    Integer update(Team department);

    Integer delete(Integer teamId);

    Integer count();

}
