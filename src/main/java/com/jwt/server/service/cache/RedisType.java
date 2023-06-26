package com.jwt.server.service.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisType {
    CLIENT_REFRESH_TOKEN("client-refresh-token-storage"),
    COMPANY_REFRESH_TOKEN("company-efresh-token-storage");

    private final String value;
}
