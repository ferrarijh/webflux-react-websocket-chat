package com.jonathan.chat.config;

import com.jonathan.chat.api.ChatRouter;
import com.jonathan.chat.dto.RoomThumbnail;
import com.jonathan.chat.handler.ChatHttpHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.socket.WebSocketHandler;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(ChatRouter.class)
public class ChatRouterTest {

    @MockBean
    private ChatHttpHandler chatHttpHandler;
    @MockBean
    private WebSocketHandler webSocketHandler;

    @Autowired
    private WebTestClient client;

    @Test
    public void test(){


        var requestBody = new RoomThumbnail(null, "JihosCrib", 0);
        this.client.post()
                .uri("http://localhost:8080/chat/room")
                .bodyValue(requestBody)
                .accept(MediaType.APPLICATION_JSON)

                .exchange()
                .expectStatus().isOk()
                .expectBody(RoomThumbnail.class);
    }
}