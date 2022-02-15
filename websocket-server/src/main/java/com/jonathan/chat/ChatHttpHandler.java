package com.jonathan.chat;

import com.jonathan.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatHttpHandler {

    private final ConcurrentMap<String, String> users;

    public Mono<ServerResponse> login(ServerRequest request){
        return request.bodyToMono(ChatMessage.class)
                .filter(msg -> msg.getType().equals(ChatMessage.Type.IN_REQ.getValue()))
                .flatMap(msg ->
                {
                    return ServerResponse.ok()
                            .bodyValue(
                                    ChatMessage.builder()
                                            .username(msg.getUsername())
                                            .type(ChatMessage.Type.IN_OK.getValue())
                                            .date(LocalDateTime.now())
                                            .build()
                            );
                });
    }
}
