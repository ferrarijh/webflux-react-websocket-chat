package com.jonathan.chat.handler;

import com.jonathan.chat.dto.ChatMessage;
import com.jonathan.chat.dto.ErrorMessage;
import com.jonathan.chat.dto.RoomThumbnail;
import com.jonathan.chat.room.LocalRoom;
import com.jonathan.chat.room.LocalRoomManager;
import com.jonathan.chat.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;

import static com.jonathan.chat.dto.ChatMessage.Type.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatHttpHandler {

    private final LocalRoomManager localRoomManager;
    private final RedisService redisService;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(ChatMessage.class)
                .filter(msg -> msg.getType().equals(IN_REQ.getValue()))
                .flatMap(msg ->
                        ServerResponse.ok()
                                .bodyValue(
                                        ChatMessage.builder()
                                                .username(msg.getUsername())
                                                .type(IN_OK.getValue())
                                                .date(LocalDateTime.now())
                                                .build()
                                )).doOnError(Throwable::printStackTrace);
    }

    public Mono<ServerResponse> createRoom(ServerRequest request) {
        return request.bodyToMono(RoomThumbnail.class)  //only title field is present here
                .flatMap(thumbnailTitleOnly -> {
                    if (thumbnailTitleOnly.getTitle().isBlank())
                        return ServerResponse.badRequest().bodyValue(new ErrorMessage("Title should not be empty"));

                    LocalRoom room = localRoomManager.createFirstRoom(thumbnailTitleOnly.getTitle());
                    RoomThumbnail responseBody = new RoomThumbnail(room.getId(), room.getTitle(), 0);
                    return redisService.createRoom(responseBody)
                            .flatMap(created -> {
                                if (created)
                                    return ServerResponse.created(URI.create("http://localhost:8080/" + responseBody.getId()))
                                            .bodyValue(responseBody);
                                else
                                    return ServerResponse.status(500).bodyValue(
                                            new ErrorMessage("Room already exists - THIS ERROR MUST NOT OCCUR")
                                    );
                            });
                });
    }

    public Mono<ServerResponse> getAllRooms(ServerRequest _req) {
        return redisService.getAllRoomThumbnails().collectList()
                .flatMap(list -> ServerResponse.ok().bodyValue(list))
                .doOnError(Throwable::printStackTrace);
    }

    public Mono<ServerResponse> getRoomThumbnail(ServerRequest request) {
        String id = request.pathVariable("id");
        return redisService.getRoomThumbnail(id)
                .flatMap(thumbnail -> {
                    if (!localRoomManager.isPresent(id))
                        localRoomManager.createRoom(thumbnail);
                    return ServerResponse.ok().bodyValue(thumbnail);
                }).switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(Throwable::printStackTrace);
    }
}