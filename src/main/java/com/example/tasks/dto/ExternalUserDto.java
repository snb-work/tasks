package com.example.tasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalUserDto {
    private Integer id;
    private String name;
    private String username;
    private String email;
}