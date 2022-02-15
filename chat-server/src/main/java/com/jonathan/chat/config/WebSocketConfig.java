package com.jonathan.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class WebSocketConfig {

    @Bean
    public ConcurrentMap<String, String> users(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Sinks.Many<String> sinksMany(){
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
