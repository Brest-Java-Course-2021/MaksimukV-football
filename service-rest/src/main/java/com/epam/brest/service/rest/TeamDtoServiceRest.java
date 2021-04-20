package com.epam.brest.service.rest;

import com.epam.brest.model.Team;
import com.epam.brest.model.dto.TeamDto;
import com.epam.brest.service.TeamDtoService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Service
public class TeamDtoServiceRest implements TeamDtoService {

    private final String url;

    private final RestTemplate restTemplate;

    public TeamDtoServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public List<TeamDto> findAllWithPrefNationality() {
        ResponseEntity responseEntity = restTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<TeamDto>>(){});
        return (List<TeamDto>) responseEntity.getBody();
    }
}
