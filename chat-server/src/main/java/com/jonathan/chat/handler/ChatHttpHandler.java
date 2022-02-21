package com.jonathan.chat.handler;

import com.jonathan.chat.dto.ChatMessage;
import com.jonathan.chat.dto.ErrorMessage;
import com.jonathan.chat.dto.RoomThumbnail;
import com.jonathan.chat.room.Room;
import com.jonathan.chat.room.RoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatHttpHandler {

    private final ConcurrentMap<String, String> users;
    private final RoomManager roomManager;

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

    public Mono<ServerResponse> createRoom(ServerRequest request){
        return request.bodyToMono(RoomThumbnail.class)
                .flatMap(thumbnail -> {
                    if(thumbnail.getTitle().isEmpty())
                        return ServerResponse.badRequest().bodyValue(
                                new ErrorMessage("Title should not be empty")
                        );

                    Room room = roomManager.createRoom(thumbnail.getTitle());
                    RoomThumbnail responseBody = new RoomThumbnail(room.getId(), room.getTitle(), room.getSize());
                    return ServerResponse.ok().bodyValue(responseBody);
                });
    }

    public Mono<ServerResponse> getRooms(ServerRequest _req){
        List<RoomThumbnail> list = roomManager.getAllRooms().stream().map(room ->
                new RoomThumbnail(room.getId(), room.getTitle(), room.getSize())
        ).collect(Collectors.toList());

        return ServerResponse.ok().bodyValue(list);
    }

    public Mono<ServerResponse> getRoomThumbnail(ServerRequest request){
        String id = request.pathVariable("id");
        Room room = roomManager.getRoom(id);
        if(room == null)
            return ServerResponse.notFound().build();
        RoomThumbnail roomThumbnail = new RoomThumbnail(room.getId(), room.getTitle(), room.getSize());
        return ServerResponse.ok().bodyValue(roomThumbnail);
    }
}
