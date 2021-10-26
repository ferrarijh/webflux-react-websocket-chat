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
    WebSocketHandler webSocketHandler(){
        return new MyWebSocketHandler();
    }

    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler handler){

        Map<String, WebSocketHandler> mapping = new HashMap<>();
        mapping.put("/chat", handler);

        return new SimpleUrlHandlerMapping(mapping, -1);
    }
}
