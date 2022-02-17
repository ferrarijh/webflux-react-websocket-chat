package com.jonathan.chat.config;

import com.jonathan.chat.handler.ChatHttpHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ChatRouter {

    @Bean
    RouterFunction<ServerResponse> route(ChatHttpHandler handler){
        return RouterFunctions.route()
                .path("/chat", builder -> builder
                        .POST("/login", accept(MediaType.APPLICATION_JSON), handler::login)
                        .GET("/rooms", accept(MediaType.APPLICATION_JSON), handler::getRooms)
                ).build();
    }

    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler handler){

        Map<String, WebSocketHandler> mapping = new HashMap<>();
        mapping.put("/chat/room", handler);

        return new SimpleUrlHandlerMapping(mapping, -1);
    }
}
