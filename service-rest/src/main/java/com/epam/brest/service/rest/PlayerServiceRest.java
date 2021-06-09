package com.epam.brest.service.rest;

import com.epam.brest.model.Player;
import com.epam.brest.service.PlayerService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceRest implements PlayerService {

    private String url;

    private RestTemplate restTemplate;

    public PlayerServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public List<Player> findAll() {
        ResponseEntity<List<Player>> responseEntity = restTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Player>>(){});
        return (List<Player>) responseEntity.getBody();
    }

    public Optional<Player> findById(Integer playerId) {
        ResponseEntity<Player> responseEntity =
                restTemplate.getForEntity(url + "/" + playerId, Player.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    public Integer create(Player player) {
        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(url, player, Integer.class);
        return (Integer) responseEntity.getBody();
    }

    public Integer update(Player player) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Player> entity = new HttpEntity<>(player, headers);
        ResponseEntity<Integer> result = restTemplate.exchange(url, HttpMethod.PUT, entity, Integer.class);
        return result.getBody();
    }

    public Integer delete(Integer playerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Player> entity = new HttpEntity<>(headers);
        ResponseEntity<Integer> result =
                restTemplate.exchange(url + "/" + playerId, HttpMethod.DELETE, entity, Integer.class);
        return result.getBody();
    }

    public Integer count() {
        return null;
    }

}
