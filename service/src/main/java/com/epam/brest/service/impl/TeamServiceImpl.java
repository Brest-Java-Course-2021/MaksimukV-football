package com.epam.brest.service.impl;

import com.epam.brest.dao.TeamDao;
import com.epam.brest.model.Team;
import com.epam.brest.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamDao teamDao;

    @Autowired
    public TeamServiceImpl(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    public List<Team> findAll() {
        return teamDao.findAll();
    }

    public Optional<Team> findById(Integer id) {
        return teamDao.findById(id);
    }

    public Integer create(Team Team) {
        return teamDao.create(Team);
    }

    public Integer update(Team Team) {
        return teamDao.update(Team);
    }

    public Integer delete(Integer TeamId) {
        return teamDao.delete(TeamId);
    }

    public Integer count() {
        return teamDao.count();
    }
}
