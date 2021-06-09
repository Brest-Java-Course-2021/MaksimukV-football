package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.TeamDao;
import com.epam.brest.model.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TeamDaoJdbc implements TeamDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamDaoJdbc.class);

    @Value("${team.select}")
    private String selectSql;

    @Value("${team.create}")
    private String createSql;

    @Value("${team.update}")
    private String updateSql;

    @Value("${team.findById}")
    private String findByIdSql;

    @Value("${team.check}")
    private String checkSql;

    @Value("${team.count}")
    private String countSql;

    @Value("${team.delete}")
    private String deleteSql;

    private RowMapper rowMapper = BeanPropertyRowMapper.newInstance(Team.class);
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TeamDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

//    public TeamDaoJdbc(NamedParameterJdbcTemplate template) {
//        this.namedParameterJdbcTemplate = template;
//    }

    public List<Team> findAll() {
        LOGGER.debug("Find all teams");
        return namedParameterJdbcTemplate.query(selectSql, rowMapper);
    }

    public Optional<Team> findById(Integer teamId) {
        LOGGER.debug("Find team by id: {}", teamId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("TEAM_ID", teamId);
        List<Team> results = namedParameterJdbcTemplate.query(findByIdSql, sqlParameterSource, rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    public Integer create(Team team) {
        LOGGER.debug("Create team: {}", team);
        if (!isTeamNameUnique(team)) {
            LOGGER.warn("Team with the same name already exists in DB: {}", team);
            throw new IllegalArgumentException("Team with the same name already exists in DB.");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("TEAM_NAME", team.getTeamName());
        namedParameterJdbcTemplate.update(createSql, sqlParameterSource, keyHolder);
        Integer teamId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        team.setTeamId(teamId);
        return teamId;
    }

    public Integer update(Team team) {
        LOGGER.debug("Update team: {}", team);
        SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("TEAM_NAME", team.getTeamName())
                        .addValue("TEAM_ID", team.getTeamId());
        return namedParameterJdbcTemplate.update(updateSql, sqlParameterSource);
    }

    public Integer delete(Integer teamId) {
        LOGGER.debug("Delete team by id: {}", teamId);
        return namedParameterJdbcTemplate.update(deleteSql, new MapSqlParameterSource()
                .addValue("TEAM_ID", teamId));
    }

    public Integer count(){
        LOGGER.debug("count()");
        return namedParameterJdbcTemplate.queryForObject(countSql, new HashMap<>(), Integer.class);
    }

    private boolean isTeamNameUnique(Team team) {
        return namedParameterJdbcTemplate.queryForObject(checkSql,
                new MapSqlParameterSource("TEAM_NAME", team.getTeamName()), Integer.class) == 0;
    }
}
