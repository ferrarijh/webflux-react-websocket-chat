package com.jonathan.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;

//@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper mapper;
    private final Sinks.Many<String> sink;
    private final ConcurrentMap<String, String> users;  //(sessionId : username)

    private WebSocketSession session;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        this.session = session;

        log.info("users.size()={}, sessionId: {}", users.size(), session.getId());

        ChatMessage init = ChatMessage.builder()
                .type(ChatMessage.Type.INIT.getValue())
                .date(LocalDateTime.now())
                .userList(new ArrayList<>(users.values()))
                .build();

        Mono<String> initMono = Mono.just(mapperWrite(init));

        session.receive()
                .map(wsm -> mapperRead(wsm.getPayloadAsText()))
                .subscribe(
                        this::onNext,
                        this::onError,
                        this::onComplete
                );

        return session.send(
                initMono.concatWith(this.sink.asFlux())
                        .map(session::textMessage)
        );
    }

    private void onNext(ChatMessage msg){
        ChatMessage.Type type = ChatMessage.Type.valueOf(msg.getType());
        log.info("onNext, type={}", type);

        switch (type){
            case USER_IN:
                handleIn(msg);
                break;
            case USER_OUT:
                handleOut(msg);
                break;
            case MESSAGE:
                handleMessage(msg);
                break;
        }
    }

    private void onError(Throwable e){
        e.printStackTrace();
        onComplete();
    }

    private void onComplete(){
        String sessionId = this.session.getId();
        String username = this.users.get(sessionId);
        this.users.remove(sessionId);

        ChatMessage msg = ChatMessage.builder()
                .type(ChatMessage.Type.MESSAGE.getValue())
                .username(username)
                .date(LocalDateTime.now())
                .build();

        this.sink.tryEmitNext(mapperWrite(msg));
    }

    private void handleIn(ChatMessage msg){
        this.users.put(this.session.getId(), msg.getUsername());
        sink.tryEmitNext(mapperWrite(msg));
    }

    private void handleOut(ChatMessage msg){
        this.users.remove(this.session.getId());
        sink.tryEmitNext(mapperWrite(msg));
    }

    private void handleMessage(ChatMessage msg){
        sink.tryEmitNext(mapperWrite(msg));
    }

    private ChatMessage mapperRead(String inbound){
        ChatMessage res = ChatMessage.builder().build();
        try{
            res = mapper.readValue(inbound, ChatMessage.class);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    private String mapperWrite(ChatMessage outbound){
        String res = "";
        try{
            res = mapper.writeValueAsString(outbound);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
