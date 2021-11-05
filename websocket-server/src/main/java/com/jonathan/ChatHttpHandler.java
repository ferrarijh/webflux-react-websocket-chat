package com.jonathan;

import com.jonathan.model.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class ChatHttpHandler {

    private final Logger log = LoggerFactory.getLogger(ChatHttpHandler.class);

    private final ConcurrentMap<String, String> users;

    public Mono<ServerResponse> login(ServerRequest request){
        return request.bodyToMono(Message.class)
                .filter(msg -> msg.getType().equals(Message.Type.IN_REQ))
                .flatMap(msg ->
                {
                    return ServerResponse.ok()
                            .bodyValue(
                                    new Message(
                                            msg.getUsername(),
                                            "",
                                            LocalDateTime.now(),
                                            Message.Type.IN_OK,
                                            new ArrayList<>(users.values())
                                    )
                            );
                });
    }
}