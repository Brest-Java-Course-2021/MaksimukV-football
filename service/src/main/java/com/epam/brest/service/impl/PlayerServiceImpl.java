package com.epam.brest.service.impl;

import com.epam.brest.dao.PlayerDao;
import com.epam.brest.model.Player;
import com.epam.brest.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService{

    private final PlayerDao playerDao;

    @Autowired
    public PlayerServiceImpl(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public List<Player> findAll() {
        return playerDao.findAll();
    }

    public Optional<Player> findById(Integer id) {
        return playerDao.findById(id);
    }

    public Integer create(Player Player) {
        return playerDao.create(Player);
    }

    public Integer update(Player Player) {
        return playerDao.update(Player);
    }

    public Integer delete(Integer PlayerId) {
        return playerDao.delete(PlayerId);
    }

    public Integer count() {
        return playerDao.count();
    }
}
