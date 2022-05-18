package com.jonathan.chat.room;

import com.jonathan.chat.dto.ChatMessage;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicInteger;

public class LocalRoom {
    private final String id;
    private final String title;
    private final Sinks.Many<ChatMessage> sink = Sinks.many().multicast().onBackpressureBuffer();

    public LocalRoom(String id, String title){
        this.id = id;
        this.title = title;
    }

    public String getId(){ return this.id; }

    public String getTitle(){ return this.title; }

    public boolean isEmpty(){
        return this.getNumOfLocalChatters() == 0;
    }

    public Sinks.EmitResult tryEmitNext(ChatMessage msg){
        return this.sink.tryEmitNext(msg);
    }

    public Flux<ChatMessage> getSinkAsFlux(){
        return this.sink.asFlux();
    }

    public int getNumOfLocalChatters(){
        return this.sink.currentSubscriberCount();
    }
}
