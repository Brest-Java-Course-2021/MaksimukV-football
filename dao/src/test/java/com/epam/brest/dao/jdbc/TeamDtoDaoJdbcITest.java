package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.TeamDtoDao;
import com.epam.brest.db.h2.SpringJdbcConfig;
import com.epam.brest.model.dto.TeamDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@Import({TeamDtoDaoJdbc.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamDtoDaoJdbcITest {

    @Autowired
    private TeamDtoDao teamDtoDao;

    @Test
    public void findAllTest() {
        List<TeamDto> teamDto = teamDtoDao.findAllWithPrefNationality();
        Assertions.assertNotNull(teamDto);
        assertTrue(teamDto.size() > 0);
    }
}