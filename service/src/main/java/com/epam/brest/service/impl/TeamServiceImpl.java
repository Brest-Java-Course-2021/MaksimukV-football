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

    private final TeamDao TeamDao;

    @Autowired
    public TeamServiceImpl(TeamDao TeamDao) {
        this.TeamDao = TeamDao;
    }

    public List<Team> findAll() {
        return TeamDao.findAll();
    }

    public Optional<Team> findById(Integer id) {
        return TeamDao.findById(id);
    }

    public Integer create(Team Team) {
        return TeamDao.create(Team);
    }

    public Integer update(Team Team) {
        return TeamDao.update(Team);
    }

    public Integer delete(Integer TeamId) {
        return TeamDao.delete(TeamId);
    }

    public Integer count() {
        return TeamDao.count();
    }
}
