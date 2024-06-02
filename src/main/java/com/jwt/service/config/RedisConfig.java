package com.jwt.service.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import static com.jwt.service.service.cache.RedisType.CLIENT_REFRESH_TOKEN;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(30L))
                .disableCachingNullValues()
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer clientRedisCacheManagerBuilderCustomizer() {
        return (builder) -> builder.withCacheConfiguration(CLIENT_REFRESH_TOKEN.getValue(),
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(30L)));
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CLIENT_REFRESH_TOKEN.getValue());
    }
}
