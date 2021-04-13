package com.epam.brest.service;

import com.epam.brest.model.dto.TeamDto;

import java.util.List;

public interface TeamDtoService {

    List<TeamDto> findAllWithPrefNationality();

}
