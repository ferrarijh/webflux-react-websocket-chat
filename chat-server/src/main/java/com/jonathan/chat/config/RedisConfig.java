package com.jonathan.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {
    @Bean
    ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("localhost", 6379);
        return new LettuceConnectionFactory(configuration);
    }
}
