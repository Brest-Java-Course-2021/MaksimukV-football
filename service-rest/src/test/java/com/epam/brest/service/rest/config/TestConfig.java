package com.epam.brest.service.rest.config;

import com.epam.brest.service.PlayerService;
import com.epam.brest.service.TeamDtoService;
import com.epam.brest.service.TeamService;
import com.epam.brest.service.rest.PlayerServiceRest;
import com.epam.brest.service.rest.TeamDtoServiceRest;
import com.epam.brest.service.rest.TeamServiceRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {

    public static final String TEAM_DTOS_URL = "http://localhost:8088/team-dtos";
    public static final String TEAMS_URL = "http://localhost:8088/teams";
    public static final String PLAYERS_URL = "http://localhost:8088/players";

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    TeamDtoService teamDtoService() {
        return new TeamDtoServiceRest(TEAM_DTOS_URL, restTemplate());
    }

    @Bean
    TeamService teamService() {
        return new TeamServiceRest(TEAMS_URL, restTemplate());
    }

    @Bean
    PlayerService playerService() {
        return new PlayerServiceRest(PLAYERS_URL, restTemplate());
    }

}
