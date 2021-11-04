package com.jonathan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.model.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
@Component
public class MyWebSocketHandler implements WebSocketHandler {

    private final Logger log = LoggerFactory.getLogger(MyWebSocketHandler.class);

    private final ObjectMapper mapper;
    private final Sinks.Many<String> sink;
    private final ConcurrentMap<String, String> users;  //(sessionId : username)

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        //subscriber receives incoming messages from session
        log.info("users.size(): "+users.size() + "session: " + session.getId());

        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(sink::tryEmitNext)
                .map(this::mapperRead)
                .subscribe(
                        msg -> handleIn(msg, session),
                        Throwable::printStackTrace,
                        () -> sink.tryEmitNext(userOutMessage(session.getId()))
                );

        return session.send(
                sink.asFlux().map(session::textMessage)
                        .doOnNext(wsm->log.info("sending from sink: "+wsm.getPayloadAsText()))
        );
    }

    String userOutMessage(String sessionId){
        String username = users.get(sessionId);
        users.remove(sessionId);
        return mapperWrite(
                new Message(username, "", LocalDateTime.now(), Message.Type.USER_OUT, new ArrayList<>())
        );
    }

    private void handleIn(Message msg, WebSocketSession session){
        if(msg.getType().equals(Message.Type.USER_IN))
            users.put(session.getId(), msg.getUsername());
    }

    private Message mapperRead(String json){
        Message res = new Message();
        log.info("json: "+json);
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

//    private static class StreamSubscriber implements Subscriber<Message> {
//
//        private final ObjectMapper mapper;
//        private final Sinks.Many<String> sink;
//
//
//        StreamSubscriber(ObjectMapper mapper, Sinks.Many<String> sink){
//            this.mapper = mapper;
//            this.sink = sink;
//        }
//
//        @Override
//        public void onSubscribe(Subscription s) {
//            System.out.println("new subscription!");
//        }
//
//        @Override
//        public void onNext(Message msg) {
//            System.out.println("onNext with content: "+msg.getContent());
//            try {
//                sink.tryEmitNext(mapper.writeValueAsString(msg));
//
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onError(Throwable t) {
//            t.printStackTrace();
//        }
//
//        @Override
//        public void onComplete() {
//            System.out.println("onComplete.");
//
//        }
//    }
}
