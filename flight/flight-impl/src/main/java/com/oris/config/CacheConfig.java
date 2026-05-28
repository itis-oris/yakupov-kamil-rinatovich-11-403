package com.oris.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableCaching
public class CacheConfig {


    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        ObjectMapper redisObjectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        redisObjectMapper.setDefaultTyping(
                new ObjectMapper.DefaultTypeResolverBuilder(
                        ObjectMapper.DefaultTyping.EVERYTHING,
                        redisObjectMapper.getPolymorphicTypeValidator()
                )
                        .init(JsonTypeInfo.Id.CLASS, null)
                        .inclusion(JsonTypeInfo.As.PROPERTY)
                        .typeProperty("@class")
        );

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(redisObjectMapper);

        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))
                        .disableCachingNullValues()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(serializer)
                        );


        return (builder) -> builder
                .cacheDefaults(defaultConfig)

                .withCacheConfiguration(CacheName.COUNTRY_LIST,
                        defaultConfig
                                .entryTtl(Duration.ofDays(7)))

                .withCacheConfiguration(CacheName.CITY_LIST,
                        defaultConfig
                                .entryTtl(Duration.ofDays(7)))

                .withCacheConfiguration(CacheName.AIRPLANE_TYPE_LIST,
                        defaultConfig
                                .entryTtl(Duration.ofDays(1)))

                .withCacheConfiguration(CacheName.AIRPORT_LIST,
                        defaultConfig
                                .entryTtl(Duration.ofHours(6)))

                .withCacheConfiguration(CacheName.SEAT_LIST,
                        defaultConfig
                                .entryTtl(Duration.ofHours(6)))

                .withCacheConfiguration(CacheName.VALIDATE_REQUEST,
                        defaultConfig
                                .entryTtl(Duration.ofMinutes(1)));
    }
}