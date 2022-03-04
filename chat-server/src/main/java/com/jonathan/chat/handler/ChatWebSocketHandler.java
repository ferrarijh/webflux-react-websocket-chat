package com.jonathan.chat.handler;

import com.jonathan.chat.dto.ChatMessage;
import com.jonathan.chat.mapper.AppMapper;
import com.jonathan.chat.room.LocalRoom;
import com.jonathan.chat.room.LocalRoomManager;
import com.jonathan.chat.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static com.jonathan.chat.dto.ChatMessage.Type.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler implements WebSocketHandler {

    private final AppMapper mapper;
    private final LocalRoomManager manager;
    private final RedisService redisService;

    private final ConcurrentMap<String, String> sessionIdToUserMap = new ConcurrentHashMap<>();

    /**
     * The client sends USER_IN message at the beginning of the connection.
     * The client receives INIT message at the beginning of the connection.
     * <p>
     * Message output flow: User Agent > WebSocketHandler > RedisService
     * Message input flow: RedisService > Room.sink > User Agent
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String[] pathArr = session.getHandshakeInfo().getUri().getPath().split("/");
        var roomId = pathArr[pathArr.length - 1];
        var room = this.manager.getRoom(roomId);

        if (!redisService.didSubscribe(roomId))
            redisService.subscribe(roomId);

        Consumer<ChatMessage> handleLocalInOut = createHandleLocalInOut(room, session);
        Consumer<SignalType> cleanup = createCleanup(room, session);

        //Received messages are published to the redis pubsub channel.
        session.receive()
                .map(wsm -> this.mapper.readChatMessage(wsm.getPayloadAsText()))
                .doOnNext(handleLocalInOut)
                .flatMap(cm -> redisService.handleChatMessage(roomId, cm))
                .subscribe();

        Mono<ChatMessage> initMono = redisService.getAllUsers(roomId).collectList()
                .map(this::createInitMessage);

        //Send messages received from the room's sink which pipes messages received from redis.
        return session.send(
                initMono.concatWith(room.getSinkAsFlux())
                        .map(cm -> session.textMessage(mapper.write(cm)))
        )
                .doOnError(Throwable::printStackTrace)
                .doFinally(cleanup)
                ;
    }


    private Consumer<ChatMessage> createHandleLocalInOut(LocalRoom localRoom, WebSocketSession session) {
        return (msg) -> {
            ChatMessage.Type type = ChatMessage.Type.valueOf(msg.getType());
            log.info("type: {}, username: {}", msg.getType(), msg.getUsername());
            if (type == USER_IN)
                this.sessionIdToUserMap.put(session.getId(), msg.getUsername());
            localRoom.handleChatMessage(msg);
        };
    }

    private Consumer<SignalType> createCleanup(LocalRoom localRoom, WebSocketSession session) {
        return (sig) -> {
            log.info("signal={}", sig.name());
            String offUsername = this.sessionIdToUserMap.get(session.getId());
            localRoom.removeUser(offUsername);
            if (localRoom.isEmpty()) {
                manager.removeRoom(localRoom);
                redisService.unsubscribe(localRoom);
            }
            this.sessionIdToUserMap.remove(session.getId());
            redisService.handleChatMessage(localRoom, createUserOutMessage(offUsername)).subscribe();
        };
    }

    private ChatMessage createInitMessage(List<String> users) {
        return ChatMessage.builder()
                .type(ChatMessage.Type.INIT.getValue())
                .date(LocalDateTime.now())
                .userList(users)
                .build();
    }

    private ChatMessage createUserOutMessage(String username) {
        return ChatMessage.builder()
                .type(ChatMessage.Type.USER_OUT.getValue())
                .date(LocalDateTime.now())
                .username(username)
                .build();
    }
}
