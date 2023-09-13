package com.jwt.server.dto.authorization;

import lombok.Data;

@Data
public class Account implements Authorization {

    private String name;

    private Long id;

    private String login;

    private String password;

    private boolean activated;

}
