package com.jonathan.chat.room;

import com.jonathan.chat.mapper.AppMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
@Component
@RequiredArgsConstructor
@Slf4j
public class LocalRoomManager {

    /* key=roomId */
    private final ConcurrentMap<String, LocalRoom> roomMap = new ConcurrentHashMap<>();

    /* key=title */
    private final ConcurrentMap<String, String> titleToRoomIdMap = new ConcurrentHashMap<>();

    private final AppMapper mapper;

    public LocalRoom createRoom(String title){
        String roomId = UUID.randomUUID().toString();
        LocalRoom newLocalRoom = new LocalRoom(roomId, title);
        this.roomMap.put(roomId, newLocalRoom);

        return newLocalRoom;
    }

    public boolean isPresent(String roomId){
        return this.roomMap.get(roomId) == null;
    }

    public LocalRoom getRoom(String roomId){
        return this.roomMap.get(roomId);
    }

    public void removeRoom(String roomId) {
        this.roomMap.remove(roomId);
    }

    public void removeRoom(LocalRoom room){
        this.roomMap.remove(room.getId());
    }
}