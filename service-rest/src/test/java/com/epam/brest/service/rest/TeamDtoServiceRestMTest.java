package com.epam.brest.service.rest;

import com.epam.brest.model.dto.TeamDto;
import com.epam.brest.service.TeamDtoService;
import com.epam.brest.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static com.epam.brest.service.rest.config.TestConfig.TEAM_DTOS_URL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class TeamDtoServiceRestMTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TeamDtoService teamDtoService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void findAllWithPrefNationality() throws Exception {
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(TEAM_DTOS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createTeamDto(0),
                                createTeamDto(1))))
                );

        // when
        List<TeamDto> teams = teamDtoService.findAllWithPrefNationality();

        // then
        mockServer.verify();
        assertNotNull(teams);
        assertTrue(teams.size() > 0);
    }

    private TeamDto createTeamDto(int index) {
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamId(index);
        teamDto.setTeamName("team" + index);
        teamDto.setPrefNationality("nteam" + index);
        return teamDto;
    }
}
