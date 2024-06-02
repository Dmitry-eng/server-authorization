package com.jwt.service.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractCacheToken implements Cache<Set<String>, String> {

    @Autowired
    protected CacheManager cacheManager;

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

    protected abstract org.springframework.cache.Cache getCache();
}
