package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.TeamDtoDao;
import com.epam.brest.model.dto.TeamDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TeamDtoDaoJdbc implements TeamDtoDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${teamDto.findAllWithPrefNationality}")
    private String findAllWithPrefNationalitySql;

    public TeamDtoDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<TeamDto> findAllWithPrefNationality() {
        List<TeamDto> teams = namedParameterJdbcTemplate.query(findAllWithPrefNationalitySql,
                BeanPropertyRowMapper.newInstance(TeamDto.class));
        return teams;
    }

}
