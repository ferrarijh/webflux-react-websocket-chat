package com.jonathan.chat.room;

import com.jonathan.chat.dto.ChatMessage;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalRoom {
    private final String id;
    private final String title;
    private final Sinks.Many<ChatMessage> sink = Sinks.many().multicast().onBackpressureBuffer();

    //TODO("TO-BE: Replace with <String(userId), User>")
    private final ConcurrentMap<String, Boolean> userMap = new ConcurrentHashMap<>();

    public LocalRoom(String id, String title){
        this.id = id;
        this.title = title;
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
        }
    }

    private void handleUserIn(ChatMessage msg){
        this.addUser(msg.getUsername());
    }

    private void handleUserOut(ChatMessage msg){
        this.removeUser(msg.getUsername());
    }

    private void addUser(String userId){
        this.userMap.putIfAbsent(userId, true);
    }

    public void removeUser(String userId){
        this.userMap.remove(userId);
    }

    public String getId(){ return this.id; }
    public String getTitle(){ return this.title; }
//    public int getSize(){ return this.userMap.size(); }
    public boolean isEmpty(){ return this.userMap.isEmpty(); }

    public Sinks.EmitResult tryEmitNext(ChatMessage msg){
        return this.sink.tryEmitNext(msg);
    }
    public Flux<ChatMessage> getSinkAsFlux(){
        return this.sink.asFlux();
    }
}
