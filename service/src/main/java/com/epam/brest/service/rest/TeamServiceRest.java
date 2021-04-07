package com.epam.brest.service.rest;

import com.epam.brest.model.Team;
import com.epam.brest.service.TeamService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceRest implements TeamService {

    private String url;

    private RestTemplate restTemplate;

    public TeamServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }
    
    public List<Team> findAll() {
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Team>) responseEntity.getBody();
    }

    public Optional<Team> findById(Integer teamId) {
        ResponseEntity<Team> responseEntity =
                restTemplate.getForEntity(url + "/" + teamId, Team.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    public Integer create(Team team) {
        ResponseEntity responseEntity = restTemplate.postForEntity(url, team, Integer.class);
        return (Integer) responseEntity.getBody();
    }

    public Integer update(Team team) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Team> entity = new HttpEntity<>(team, headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url, HttpMethod.PUT, entity, Integer.class);
        return result.getBody();
    }

    public Integer delete(Integer teamId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Team> entity = new HttpEntity<>(headers);
        ResponseEntity<Integer> result =
                restTemplate.exchange(url + "/" + teamId, HttpMethod.DELETE, entity, Integer.class);
        return result.getBody();
    }

    public Integer count() {
        return null;
    }
    
}
