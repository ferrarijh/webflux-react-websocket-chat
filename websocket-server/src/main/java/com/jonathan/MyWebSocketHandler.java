package com.jonathan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;

public class MyWebSocketHandler implements WebSocketHandler {

    private final Logger log = LoggerFactory.getLogger(MyWebSocketHandler.class);

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(
                session.receive().doOnNext(wsMessage -> log.info("received: "+wsMessage.getPayloadAsText()))
        );
    }
}
