package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.TeamDtoDao;
import com.epam.brest.model.dto.TeamDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TeamDtoDaoJdbc implements TeamDtoDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamDtoDaoJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${teamDto.findAllWithPrefNationality}")
    private String findAllWithPrefNationalitySql;

    public TeamDtoDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

//    public TeamDtoDaoJdbc(NamedParameterJdbcTemplate template) {
//        this.namedParameterJdbcTemplate = template;
//    }

    public List<TeamDto> findAllWithPrefNationality() {
        LOGGER.debug("findAllWithPrefNationality()");
        List<TeamDto> teams = namedParameterJdbcTemplate.query(findAllWithPrefNationalitySql,
                BeanPropertyRowMapper.newInstance(TeamDto.class));
        return teams;
    }

}
