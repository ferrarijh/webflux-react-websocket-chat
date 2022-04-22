package com.jonathan.chat.gw.router;

import com.jonathan.chat.gw.handler.GWHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Client requests api path `/chat/validate-token` only from `/chat/login` or `/chat/join`
 */
@Configuration
public class GWRouter {

    @Bean
    RouterFunction<ServerResponse> route(GWHandler handler) {
        return RouterFunctions.route()
                .path("/chat", builder -> builder
                        .GET("/validate-token", handler::handleInitialAuthentication)
                        .GET("/remove-token", handler::handleRemoveToken)
                        .build()
                ).build();
    }
}
