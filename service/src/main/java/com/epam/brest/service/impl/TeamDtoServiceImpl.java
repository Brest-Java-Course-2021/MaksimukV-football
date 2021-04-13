package com.epam.brest.service.impl;

import com.epam.brest.dao.TeamDtoDao;
import com.epam.brest.model.dto.TeamDto;
import com.epam.brest.service.TeamDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeamDtoServiceImpl implements TeamDtoService {

    private final TeamDtoDao teamDtoDao;


    public TeamDtoServiceImpl(TeamDtoDao teamDtoDao) {
        this.teamDtoDao = teamDtoDao;
    }

    public List<TeamDto> findAllWithPrefNationality(){return teamDtoDao.findAllWithPrefNationality();}

}
