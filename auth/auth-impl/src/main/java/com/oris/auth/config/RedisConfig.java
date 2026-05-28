package com.oris.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.oris.auth.model.RefreshTokenData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper mapper;

    @Bean
    public RedisTemplate<String, RefreshTokenData> redisRefreshTokenTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, RefreshTokenData> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<RefreshTokenData> serializer =
                new Jackson2JsonRedisSerializer<>(mapper, RefreshTokenData.class);
        template.setValueSerializer(serializer);
        return template;
    }

    @Bean
    public RedisTemplate<String, String> redisRefreshTokenSetTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }
}
