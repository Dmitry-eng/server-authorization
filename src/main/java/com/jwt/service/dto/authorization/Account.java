package com.jwt.service.dto.authorization;

import lombok.Data;

import java.util.Set;

@Data
public class Account {

    private String login;

    private String password;

    private Set<String> roles;

}
