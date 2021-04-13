package com.epam.brest.dao;

import com.epam.brest.model.dto.TeamDto;

import java.util.List;

public interface TeamDtoDao {

    List<TeamDto> findAllWithPrefNationality();

}
