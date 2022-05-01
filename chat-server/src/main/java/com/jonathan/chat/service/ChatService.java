package com.jonathan.chat.service;

import com.jonathan.chat.dto.ChatMessage;
import com.jonathan.chat.dto.RoomThumbnail;
import com.jonathan.chat.mapper.AppMapper;
import com.jonathan.chat.room.LocalRoom;
import com.jonathan.chat.room.LocalRoomManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jonathan.chat.dto.ChatMessage.Type.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final AppMapper mapper;
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final LocalRoomManager localRoomManager;
    private final ConcurrentHashMap<String, Disposable> subscriptions = new ConcurrentHashMap<>();  //key=roomId

    /**
     * Subscribe to a message channel associated with the room id. After subscription, the app starts receiving and
     * sending messages from and to the room. Incoming messages are piped to respective room's sink. Subscription is idempotent.
     */
    public void subscribe(String roomId) {
        subscriptions.computeIfAbsent(roomId, rid ->
                redisTemplate.listenToChannel(rid)
                        .doOnNext(msg -> {
                            ChatMessage chatMessage = mapper.readChatMessage(msg.getMessage());
                            localRoomManager.getRoom(rid).tryEmitNext(chatMessage);
                        }).doOnError(Throwable::printStackTrace)
                        .subscribe()
        );
    }

    public void unsubscribe(String roomId) {
        subscriptions.get(roomId).dispose();
        subscriptions.remove(roomId);
    }

    public void unsubscribe(LocalRoom room) {
        this.unsubscribe(room.getId());
    }

    public boolean didSubscribe(String roomId) {
        return subscriptions.get(roomId) != null;
    }

    /**
     * Create mapping with key=room:${ROOM_ID}:title and value in redis.
     *
     * @return true if the room is created, false otherwise(i.e. room already exists in redis).
     */
    public Mono<Boolean> createRoom(RoomThumbnail thumbnail) {
        return redisTemplate.opsForValue().setIfAbsent(roomTitleKey(thumbnail.getId()), thumbnail.getTitle());
    }

    /**
     * If message type is USER_IN, add the user to the zset associated with roomId.
     * If the type is USER_OUT, remove the user from the zset.
     * Finally, pipe the message to respective pubsub channel.
     *
     * @return the number of clients that received the message
     */
    public Mono<Long> handleChatMessage(String roomId, ChatMessage message) {
        ChatMessage.Type type = ChatMessage.Type.valueOf(message.getType());
        String roomUsersKey = roomUsersKey(roomId);
        String roomTitleKey = roomTitleKey(roomId);

        Mono<Void> zsetOp;
        switch (type) {
            case USER_IN:
                zsetOp = redisTemplate.opsForZSet()
                        .add(roomUsersKey, message.getUsername(), System.currentTimeMillis())
                        .then();
                break;
            case USER_OUT:
                zsetOp = redisTemplate.opsForZSet()
                        .remove(roomUsersKey, message.getUsername())
                        .then(redisTemplate.hasKey(roomUsersKey)
                                .filter(ex -> !ex)

                                //delete room title if there's no user in the zset.
                                .flatMap(__ -> redisTemplate.opsForZSet().delete(roomTitleKey))
                        ).then();
                break;
            default:
                zsetOp = Mono.empty();
        }

        return zsetOp.then(redisTemplate.convertAndSend(roomId, mapper.write(message)));
    }

    public Flux<String> getAllUsers(String roomId) {
        return redisTemplate.opsForZSet().range(roomUsersKey(roomId), Range.unbounded());
    }

    public Flux<RoomThumbnail> getAllRoomThumbnails() {
        return redisTemplate.scan(ScanOptions.scanOptions().match("room:*:users").count(Integer.MAX_VALUE).build())
                .flatMap(roomUsersKey -> {
                    String roomId = roomUsersKey.split(":")[1];
                    return Mono.zip(
                            Mono.just(roomId),
                            redisTemplate.opsForValue().get(roomTitleKey(roomId)),
                            redisTemplate.opsForZSet().count(roomUsersKey, Range.unbounded())
                    );
                }).map(tuple -> new RoomThumbnail(tuple.getT1(), tuple.getT2(), tuple.getT3().intValue()));
    }

    /**
     * Returns RoomThumbnail with room id, title and size if the room associated with the id exists in redis. Otherwise an empty Mono.
     */
    public Mono<RoomThumbnail> getRoomThumbnail(String roomId) {
        return Mono.zip(
                redisTemplate.opsForValue().get(roomTitleKey(roomId)),
                redisTemplate.opsForZSet().count(roomUsersKey(roomId), Range.unbounded())
        ).flatMap(tuple2 -> {
            if (tuple2.getT2() == 0)
                return Mono.empty();
            else
                return Mono.just(new RoomThumbnail(roomId, tuple2.getT1(), tuple2.getT2().intValue()));
        });
    }

    private String roomTitleKey(String id) {
        return "room:" + id + ":title";
    }

    private String roomUsersKey(String id) {
        return "room:" + id + ":users";
    }

    //================================ LocalRoomManager ================================

    public LocalRoom getRoom(String id) {
        return localRoomManager.getRoom(id);
    }

    public void removeLocalRoom(String roomId) {
        this.localRoomManager.removeRoom(roomId);
        this.unsubscribe(roomId);
    }

    public LocalRoom createLocalRoom(String roomId){
        return this.localRoomManager.createRoom(roomId, null);
    }

}