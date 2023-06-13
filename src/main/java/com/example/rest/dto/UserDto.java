package com.example.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {
    private String login;
    private String password;
    private String country;
    private String city;
    private String street;
    private String house;
}
