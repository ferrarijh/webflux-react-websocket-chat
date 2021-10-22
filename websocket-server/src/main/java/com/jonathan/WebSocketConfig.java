package com.jonathan;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

    @Bean
    public HandlerMapping handlerMapping(){

        Map<String, WebSocketHandler> mapping = new HashMap<>();
        mapping.put("/chat", new MyWebSocketHandler());

        return new SimpleUrlHandlerMapping(mapping, -1);
    }
}
