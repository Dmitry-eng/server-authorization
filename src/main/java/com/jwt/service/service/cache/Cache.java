package com.jwt.service.service.cache;

public interface Cache<T, E> {
    void add(String key, E object);

    T get(String key);

    void remove(String key, String value);
}
