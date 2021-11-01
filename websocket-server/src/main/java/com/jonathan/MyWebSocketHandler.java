package com.jonathan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.model.Message;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
@Component
public class MyWebSocketHandler implements WebSocketHandler {

    private final Logger log = LoggerFactory.getLogger(MyWebSocketHandler.class);

    private final ObjectMapper mapper;
    private final Sinks.Many<String> sink;
    private final ConcurrentMap<String, WebSocketSession> users;

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        //subscriber receives incoming messages from session
        StreamSubscriber subscriber = new StreamSubscriber(mapper, sink);

        session.receive()
                .map(wsm -> this.mapperRead(wsm.getPayloadAsText()))
                .doOnNext(msg -> handleUserInOut(msg, session))
                .subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);

        return session.send(
                sink.asFlux().map(session::textMessage)
                        .doOnNext(wsm->log.info("sending from sink: "+wsm.getPayloadAsText()))
        );
    }

    private void handleUserInOut(Message msg, WebSocketSession session){
        if(msg.getType().equals(Message.Type.USER_IN)) {
            users.put(msg.getUsername(), session);
            session.send(Mono.just(new ArrayList<>(users.keySet()))
                    .map(list -> this.listToWebSocketMessage(list, session))
            ).subscribe();
        }
        else if(msg.getType().equals(Message.Type.USER_OUT))
            users.remove(msg.getUsername());
    }

    private WebSocketMessage listToWebSocketMessage(List<String> list, WebSocketSession session){
        Message msg = new Message();
        msg.setType(Message.Type.USER_LIST);
        msg.setUserList(list);
        String json = mapperWrite(msg);
        return session.textMessage(json);
    }

    private Message mapperRead(String json){
        Message res = new Message();
        try{
            res = mapper.readValue(json, Message.class);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    private String mapperWrite(Message msg){
        String res = "";
        try{
            res = mapper.writeValueAsString(msg);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    private static class StreamSubscriber implements Subscriber<Message> {

        private final ObjectMapper mapper;
        private final Sinks.Many<String> sink;

        StreamSubscriber(ObjectMapper mapper, Sinks.Many<String> sink){
            this.mapper = mapper;
            this.sink = sink;
        }

        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("new subscription!");
        }

        @Override
        public void onNext(Message msg) {
            try {
                sink.tryEmitNext(mapper.writeValueAsString(msg));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }

        @Override
        public void onComplete() {
            System.out.println("onComplete.");

        }
    }
}
