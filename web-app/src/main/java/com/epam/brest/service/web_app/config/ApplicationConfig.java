package com.epam.brest.service.web_app.config;

import com.epam.brest.service.PlayerService;
import com.epam.brest.service.TeamDtoService;
import com.epam.brest.service.TeamService;
import com.epam.brest.service.rest.PlayerServiceRest;
import com.epam.brest.service.rest.TeamDtoServiceRest;
import com.epam.brest.service.rest.TeamServiceRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    TeamDtoService teamDtoService() {
        String url = String.format("%s://%s:%d/team-dtos", protocol, host, port);
        return new TeamDtoServiceRest(url, restTemplate());
    };

    @Bean
    TeamService teamService() {
        String url = String.format("%s://%s:%d/teams", protocol, host, port);
        return new TeamServiceRest(url, restTemplate());
    };

    @Bean
    PlayerService playerService() {
        String url = String.format("%s://%s:%d/players", protocol, host, port);
        return new PlayerServiceRest(url, restTemplate());
    };
}
