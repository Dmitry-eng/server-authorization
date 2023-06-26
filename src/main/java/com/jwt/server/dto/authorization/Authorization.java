package com.jwt.server.dto.authorization;

import com.jwt.server.dto.AuthType;

public interface Authorization {
    String getLogin();

    String getPassword();

    AuthType getAccountType();
}
