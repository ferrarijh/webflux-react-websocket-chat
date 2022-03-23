package com.jonathan.chat.room;

import com.jonathan.chat.dto.ChatMessage;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalRoom {
    private final String id;
    private final String title;
    private final Sinks.Many<ChatMessage> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final AtomicInteger connCnt = new AtomicInteger(0);

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
        this.connCnt.incrementAndGet();
    }

    private void handleUserOut(ChatMessage msg){
        this.connCnt.decrementAndGet();
    }

    public void removeUser(String userId){
        this.connCnt.decrementAndGet();
    }

    public String getId(){ return this.id; }

    public String getTitle(){ return this.title; }

    public boolean isEmpty(){
        return this.connCnt.get() == 0;
    }

    public Sinks.EmitResult tryEmitNext(ChatMessage msg){
        return this.sink.tryEmitNext(msg);
    }

    public Flux<ChatMessage> getSinkAsFlux(){
        return this.sink.asFlux();
    }
}
