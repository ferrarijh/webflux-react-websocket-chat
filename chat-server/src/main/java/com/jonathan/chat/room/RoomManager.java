package com.jonathan.chat.room;

import com.jonathan.chat.mapper.AppMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
@Component
@RequiredArgsConstructor
public class RoomManager {
    private final ConcurrentMap<String, Room> roomMap = new ConcurrentHashMap<>();
    private final AppMapper mapper;

    public Room createRoom(String roomId){
        Room newRoom = new Room(roomId, mapper);
        this.roomMap.put(roomId, newRoom);
        return newRoom;
    }

    public boolean isRoomPresent(String roomId){
        return roomMap.get(roomId) != null;
    }

    public List<String> getAllUsers(String roomId){
        return roomMap.get(roomId).getAllUsers();
    }

    public Room getRoom(String roomId){
        return roomMap.get(roomId);
    }

    public List<String> getAllRooms(){
        return new ArrayList<String>(roomMap.keySet());
    }
}