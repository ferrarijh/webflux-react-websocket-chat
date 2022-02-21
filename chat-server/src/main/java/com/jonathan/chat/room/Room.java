package com.jonathan.chat.room;

import com.jonathan.chat.dto.ChatMessage;
import com.jonathan.chat.mapper.AppMapper;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Room {
    private final String id;
    private final String title;
    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    //TODO("TO-BE: replace to <String, User>")
    private final ConcurrentMap<String, Boolean> userMap = new ConcurrentHashMap<>();

    private final AppMapper mapper;

    public Room(String id, String title, AppMapper mapper){
        this.id = id;
        this.title = title;
        this.mapper = mapper;
    }

    public String addUser(String userId){
        try{
            userMap.put(userId, true);
            return userId;
        }catch(NullPointerException npe){
            npe.printStackTrace();
            return null;
        }
    }

    public void removeUser(String userId){
        this.userMap.remove(userId);
    }

    public List<String> getAllUsers(){
        return new ArrayList<>(this.userMap.keySet());
    }

    public void handleChatMessage(ChatMessage msg){
        ChatMessage.Type type = ChatMessage.Type.valueOf(msg.getType());
        switch(type){
            case USER_IN:
                handleUserIn(msg);
                break;
            case USER_OUT:
                handleUserOut(msg);
                break;
            case MESSAGE:
                handleMessage(msg);
                break;
        }
    }

    private void handleUserIn(ChatMessage msg){
        this.addUser(msg.getUsername());
        sink.tryEmitNext(mapper.write(msg));
    }

    private void handleUserOut(ChatMessage msg){
        this.removeUser(msg.getUsername());
        sink.tryEmitNext(mapper.write(msg));
    }

    private void handleMessage(ChatMessage msg){
        sink.tryEmitNext(mapper.write(msg));
    }

    public String getId(){ return this.id; }
    public String getTitle(){ return this.title; }
    public int getSize(){ return this.userMap.size(); }
    public Sinks.Many<String> getSink(){ return this.sink; }
}
