package com.jwt.server.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.jwt.server.service.cache.RedisType.CLIENT_REFRESH_TOKEN;


@Service
@RequiredArgsConstructor
public class CacheToken implements Cache<Set<String>, String> {

    private final CacheManager cacheManager;

    @Override
    public void add(String key, String object) {
        Set<String> refreshTokens = get(key);
        refreshTokens.add(object);
        getCache().putIfAbsent(key, refreshTokens);
    }

    @Override
    public Set<String> get(String key) {
        return Optional.of(getCache())
                .map(cache -> cache.get(key, Set.class))
                .orElse(new HashSet<String>());
    }

    @Override
    public void remove(String key, String value) {
        Set<?> tokens = Optional.ofNullable(getCache().get(key, Set.class)).orElse(Collections.emptySet());
        tokens.remove(value);
    }

    private org.springframework.cache.Cache getCache() {
        return cacheManager.getCache(CLIENT_REFRESH_TOKEN.getValue());
    }
}
