package com.jwt.server.dto.authorization;

import lombok.Data;

@Data
public abstract class AbstractModel implements Authorization {

    private Long id;

    private String login;

    private String password;

    private Boolean activated;

}
