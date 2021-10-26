package com.jonathan;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SessionManager {

    @Bean
    ConcurrentMap<String, WebSocketSession> sessions(){
        return new ConcurrentHashMap<>();
    }

}
