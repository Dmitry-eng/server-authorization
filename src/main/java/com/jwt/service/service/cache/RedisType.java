package com.jwt.service.service.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisType {
    CLIENT_REFRESH_TOKEN("client-refresh-token-storage");

    private final String value;
}
