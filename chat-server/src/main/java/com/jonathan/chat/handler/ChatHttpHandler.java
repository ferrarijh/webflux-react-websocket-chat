package com.jonathan.chat.handler;

import com.jonathan.chat.dto.ErrorMessage;
import com.jonathan.chat.dto.RoomList;
import com.jonathan.chat.dto.RoomThumbnail;
import com.jonathan.chat.room.LocalRoomManager;
import com.jonathan.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class ChatHttpHandler {

    private final ChatService chatService;

    public Mono<ServerResponse> createRoom(ServerRequest request) {
        return request.bodyToMono(RoomThumbnail.class)
                .flatMap(thumbnailTitleOnly -> {    //only title field is present here
                    if (thumbnailTitleOnly.getTitle().isBlank())
                        return ServerResponse.badRequest().bodyValue(new ErrorMessage("Title should not be empty"));

                    String newRoomId = UUID.randomUUID().toString();
                    RoomThumbnail newRoomThumbnail = new RoomThumbnail(newRoomId, thumbnailTitleOnly.getTitle(), 0);
                    return chatService.createRoom(newRoomThumbnail)
                            .flatMap(created -> {
                                if (created)    //TODO("set host address from eureka")
                                    return ServerResponse.created(URI.create("http://127.0.0.1:8080/"+newRoomThumbnail.getId()))
                                            .bodyValue(newRoomThumbnail);
                                else
                                    return ServerResponse.status(500).bodyValue(
                                            new ErrorMessage("Room already exists - THIS ERROR MUST NOT OCCUR")
                                    );
                            });
                });
    }

    public Mono<ServerResponse> getAllRooms(ServerRequest _req) {
        return chatService.getAllRoomThumbnails().collectList()
                .flatMap(list -> ServerResponse.ok().bodyValue(new RoomList(list)))
                .doOnError(Throwable::printStackTrace);
    }

    public Mono<ServerResponse> getRoomThumbnail(ServerRequest request) {
        String id = request.pathVariable("id");
        return chatService.getRoomThumbnail(id)
                .flatMap(thumbnail -> {
                    if (!chatService.isLocalRoomPresent(id))
                        chatService.createLocalRoom(thumbnail);
                    return ServerResponse.ok().bodyValue(thumbnail);
                }).switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(Throwable::printStackTrace);
    }
}