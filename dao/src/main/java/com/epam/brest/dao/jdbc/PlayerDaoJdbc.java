package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.PlayerDao;
import com.epam.brest.model.Player;

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
public class PlayerDaoJdbc implements PlayerDao {

    @Value("${player.select}")
    private String selectSql;

    @Value("${player.create}")
    private String createSql;

    @Value("${player.update}")
    private String updateSql;

    @Value("${player.findById}")
    private String findByIdSql;

    @Value("${player.check}")
    private String checkSql;

    @Value("${player.count}")
    private String countSql;

    @Value("${player.delete}")
    private String deleteSql;

    private RowMapper rowMapper = BeanPropertyRowMapper.newInstance(Player.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Player> findAll() {
        return namedParameterJdbcTemplate.query(selectSql, rowMapper);
    }

    public Optional<Player> findById(Integer playerId) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("PLAYER_ID", playerId);
        List<Player> results = namedParameterJdbcTemplate.query(findByIdSql, sqlParameterSource, rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    public Integer create(Player player) {
        if (!isPlayerNameUnique(player)) {
            throw new IllegalArgumentException("Player with the same name already exists in DB.");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("FIRSTNAME", player.getFirstName())
                        .addValue("LASTNAME", player.getLastName())
                        .addValue("NATIONALITY", player.getNationality())
                        .addValue("SALARY", player.getSalary())
                        .addValue("TEAM_ID", player.getTeamId());
        namedParameterJdbcTemplate.update(createSql, sqlParameterSource, keyHolder);
        Integer playerId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        player.setPlayerId(playerId);
        return playerId;
    }

    public Integer update(Player player) {
        SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("PLAYER_ID", player.getPlayerId())
                        .addValue("FIRSTNAME", player.getFirstName())
                        .addValue("LASTNAME", player.getLastName())
                        .addValue("NATIONALITY", player.getNationality())
                        .addValue("SALARY", player.getSalary())
                        .addValue("TEAM_ID", player.getTeamId());
        return namedParameterJdbcTemplate.update(updateSql, sqlParameterSource);
    }

    public Integer delete(Integer playerId) {
        return namedParameterJdbcTemplate.update(deleteSql, new MapSqlParameterSource()
                .addValue("PLAYER_ID", playerId));
    }

    public Integer count(){
        return namedParameterJdbcTemplate.queryForObject(countSql, new HashMap<>(), Integer.class);
    }

    private boolean isPlayerNameUnique(Player player) {
        return namedParameterJdbcTemplate.queryForObject(checkSql,
                new MapSqlParameterSource("FIRSTNAME", player.getFirstName()), Integer.class) == 0;
    }
}
