package com.jonathan.chat.handler;

import com.jonathan.chat.dto.ChatMessage;
import com.jonathan.chat.mapper.AppMapper;
import com.jonathan.chat.room.LocalRoom;
import com.jonathan.chat.service.ChatService;
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

/**
 * Client sends USER_IN message at the beginning of the connection and receives INIT message at the beginning of the
 * connection.
 * Message output flow: User Agent > WebSocketHandler > RedisService
 * Message input flow: RedisService > Room.sink > User Agent
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler implements WebSocketHandler {

    private final AppMapper mapper;
    private final ChatService chatService;

    private final ConcurrentMap<String, String> sessionIdToUserMap = new ConcurrentHashMap<>();

    /**
     * connCnt per room is incremented here.
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String[] pathArr = session.getHandshakeInfo().getUri().getPath().split("/");
        String roomId = pathArr[pathArr.length - 1];
        LocalRoom room;
        if((room = this.chatService.getRoom(roomId)) == null)
            room = this.chatService.createLocalRoom(roomId);

        if (!chatService.didSubscribe(roomId))
            chatService.subscribe(roomId);


        //Received messages are published to the redis pubsub channel.
        session.receive()
                .map(wsm -> this.mapper.readChatMessage(wsm.getPayloadAsText()))
                .doOnNext(msg -> {
                    ChatMessage.Type type = ChatMessage.Type.valueOf(msg.getType());
                    if(type == USER_IN)
                        this.sessionIdToUserMap.put(session.getId(), msg.getUsername());
                }).flatMap(msg -> this.chatService.handleChatMessage(roomId, msg))
                .subscribe();

        Mono<ChatMessage> initMono = chatService.getAllUsers(roomId).collectList()
                .map(this::createInitMessage);

        Consumer<SignalType> cleanup = createCleanup(room, session);

        //Send messages received from the room's sink which pipes messages received from redis.
        return session.send(
                initMono.concatWith(room.getSinkAsFlux())
                        .map(cm -> session.textMessage(mapper.write(cm))).log()
        )
                .doOnError(Throwable::printStackTrace)
                .doFinally(cleanup)
                ;
    }

    /**
     * Decrement connCnt of the LocalRoom instance. If connCnt == 0 afterwards, remove the room chatService.
     */
    private Consumer<SignalType> createCleanup(LocalRoom localRoom, WebSocketSession session) {
        return (sig) -> {
            log.info("signal={}", sig.name());
            String offUsername = this.sessionIdToUserMap.get(session.getId());
            localRoom.removeUser(offUsername);
            if (localRoom.isEmpty())
                chatService.removeLocalRoom(localRoom.getId());
            this.sessionIdToUserMap.remove(session.getId());
            chatService.handleChatMessage(localRoom.getId(), createUserOutMessage(offUsername)).subscribe();
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
