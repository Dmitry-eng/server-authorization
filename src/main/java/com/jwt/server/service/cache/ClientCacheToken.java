package com.jwt.server.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.Cache;

import static com.jwt.server.service.cache.RedisType.CLIENT_REFRESH_TOKEN;

@Service(value = "clientCacheToken")
@RequiredArgsConstructor
public class ClientCacheToken extends AbstractCacheToken {

    @Autowired
    protected Cache getCache() {
        return cacheManager.getCache(CLIENT_REFRESH_TOKEN.getValue());
    }
}