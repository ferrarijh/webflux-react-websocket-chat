package com.jonathan.chat.room;

import com.jonathan.chat.mapper.AppMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
@Component
@RequiredArgsConstructor
public class RoomManager {
    /* key=roomId */
    private final ConcurrentMap<String, Room> roomMap = new ConcurrentHashMap<>();
    /* key=title */
    private final ConcurrentMap<String, String> titleToRoomIdMap = new ConcurrentHashMap<>();
    private final AppMapper mapper;

    public Room createRoom(String title){
        String roomId = UUID.randomUUID().toString();
        Room newRoom = new Room(roomId, title, this.mapper);
        this.roomMap.put(roomId, newRoom);
        return newRoom;
    }

    public boolean isRoomPresent(String roomId){
        return this.roomMap.get(roomId) != null;
    }

    public List<String> getAllUsers(String roomId){
        return this.roomMap.get(roomId).getAllUsers();
    }

    public Room getRoom(String roomId){
        return this.roomMap.get(roomId);
    }

    public List<Room> getAllRooms(){
        return new ArrayList<>(this.roomMap.values());
    }

    public void removeRoom(String roomId) {
        this.roomMap.remove(roomId);
    }
}