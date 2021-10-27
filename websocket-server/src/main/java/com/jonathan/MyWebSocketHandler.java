package com.jonathan;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.Scannable;
import reactor.core.publisher.*;

public class MyWebSocketHandler implements WebSocketHandler {

    private final Logger log = LoggerFactory.getLogger(MyWebSocketHandler.class);

    private static final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        //subscriber receives incoming messages from session
        StreamSubscriber subscriber = new StreamSubscriber();

        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnEach(str->log.info(session.getId()+" received: "+str))
                .subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);

        return session.send(
                sink.asFlux().map(session::textMessage)
                        .doOnNext(wsm->log.info("sending from sink: "+wsm.getPayloadAsText()))
        );
    }

    private static class StreamSubscriber implements Subscriber<String> {

        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("new subscription!");
        }

        @Override
        public void onNext(String s) {
            MyWebSocketHandler.sink.tryEmitNext(s);
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
