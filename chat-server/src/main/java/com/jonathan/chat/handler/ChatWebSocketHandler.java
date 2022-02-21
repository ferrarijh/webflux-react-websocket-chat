package com.jonathan.chat.handler;

import com.jonathan.chat.dto.ChatMessage;
import com.jonathan.chat.mapper.AppMapper;
import com.jonathan.chat.room.Room;
import com.jonathan.chat.room.RoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler implements WebSocketHandler {

    private final AppMapper mapper;
    private final RoomManager manager;

    private final ConcurrentMap<String, String> sessionIdToUserMap = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String[] pathArr = session.getHandshakeInfo().getUri().getPath().split("/");

        var roomId = pathArr[pathArr.length - 1];
        var room = this.manager.getRoom(roomId);

        Consumer<ChatMessage> onNext = (msg) -> {
            ChatMessage.Type type = ChatMessage.Type.valueOf(msg.getType());
            log.info("type: {}, username: {}", msg.getType(), msg.getUsername());
            if (type == ChatMessage.Type.USER_IN)
                this.sessionIdToUserMap.put(session.getId(), msg.getUsername());
            room.handleChatMessage(msg);
        };

        Runnable onComplete = () -> {
            String username = this.sessionIdToUserMap.get(session.getId());
            if(username != null)
                room.removeUser(username);
            if(room.getSize() == 0)
                this.manager.removeRoom(room.getId());
            else{
                var userOut = this.createUserOutMessage(username);
                room.handleChatMessage(userOut);
            }
        };

        Consumer<Throwable> onError = (e) -> {
            e.printStackTrace();
            onComplete.run();
        };

        session.receive()
                .map(wsm -> this.mapper.read(wsm.getPayloadAsText()))
                .subscribe(
                        onNext,
                        onError,
                        onComplete
                );

        Mono<String> initMono = Mono.just(createInitMessage(roomId));
        return session.send(
                initMono.concatWith(room.getSink().asFlux())
                        .map(session::textMessage)
        );
    }

    private String createInitMessage(String roomId){
        ChatMessage init = ChatMessage.builder()
                .type(ChatMessage.Type.INIT.getValue())
                .date(LocalDateTime.now())
                .userList(this.manager.getAllUsers(roomId))
                .build();
        return mapper.write(init);
    }

    private ChatMessage createUserOutMessage(String username){
        return ChatMessage.builder()
                .type(ChatMessage.Type.USER_OUT.getValue())
                .date(LocalDateTime.now())
                .username(username)
                .build();
    }
}
