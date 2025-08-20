package com.example.tasks.service;

import com.example.tasks.dto.ExternalUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalApiService {

    private final RestTemplate restTemplate;

    @Value("${external.api.url:https://jsonplaceholder.typicode.com}")
    private String externalApiUrl;

    public List<ExternalUserDto> getExternalUsers() {
        String url = externalApiUrl + "/users";
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExternalUserDto>>() {}
        ).getBody();
    }
}
