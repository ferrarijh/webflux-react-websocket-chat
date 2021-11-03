package com.jonathan.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class WebSocketConfig {

    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler handler){

        Map<String, WebSocketHandler> mapping = new HashMap<>();
        mapping.put("/chat", handler);

        return new SimpleUrlHandlerMapping(mapping, -1);
    }

    @Bean
    public ConcurrentMap<String, WebSocketSession> users(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Sinks.Many<String> sinksMany(){
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
