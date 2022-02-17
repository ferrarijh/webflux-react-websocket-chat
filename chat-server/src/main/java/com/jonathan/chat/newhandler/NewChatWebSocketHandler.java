package com.jonathan.chat.newhandler;

import com.jonathan.chat.dto.ChatMessage;
import com.jonathan.chat.mapper.AppMapper;
import com.jonathan.chat.room.Room;
import com.jonathan.chat.room.RoomManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class NewChatWebSocketHandler implements WebSocketHandler {

    private final AppMapper mapper;
    private final RoomManager manager;

    private Room room;
    private String userId;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String[] pathArr = session.getHandshakeInfo().getUri().getPath().split("/");
        String roomId = pathArr[pathArr.length-1];

        if(!this.manager.isRoomPresent(roomId))
            this.room = this.manager.createRoom(roomId);
        else
            this.room = this.manager.getRoom(roomId);

        session.receive()
                .map(wsm -> this.mapper.read(wsm.getPayloadAsText()))
                .subscribe(
                        this::onNext,
                        this::onError,
                        this::onComplete
                );

        Mono<String> initMono = Mono.just(createInitMessage());
        return session.send(
                initMono.concatWith(room.getSink().asFlux())
                        .map(session::textMessage)
        );
    }

    private void onNext(ChatMessage msg){
        ChatMessage.Type type = ChatMessage.Type.valueOf(msg.getType());
        if(type == ChatMessage.Type.INIT)
            this.userId = msg.getUsername();
        room.handleChatMessage(msg);
    }

    private void onError(Throwable e){
        e.printStackTrace();
        if(userId != null)
            this.room.removeUser(this.userId);
    }

    private void onComplete(){
        if(userId != null)
            this.room.removeUser(this.userId);
    }

    private String createInitMessage(){
        ChatMessage init = ChatMessage.builder()
                .type(ChatMessage.Type.INIT.getValue())
                .date(LocalDateTime.now())
                .userList(this.manager.getAllUsers(this.room.getId()))
                .build();
        return mapper.write(init);
    }
}
