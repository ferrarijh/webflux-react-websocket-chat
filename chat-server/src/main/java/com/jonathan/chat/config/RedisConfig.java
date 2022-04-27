package com.jonathan.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {

    private final AppProperties props;

    @Bean
    ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(){
        log.info("redisHost={}", props.getRedisHost());
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(props.getRedisHost(), 6379);
        return new LettuceConnectionFactory(configuration);
    }
}
