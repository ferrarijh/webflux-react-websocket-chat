package com.jonathan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class MyRouter {
//    @Bean
//    RouterFunction<ServerResponse> route(MyHandler handler){
//        return RouterFunctions.route()
//                .path("/api/v1", builder -> builder
//                        .GET
//                )
//                .build();
//    }

    @Bean
    RouterFunction<ServerResponse> route(MyHandler handler){
        return RouterFunctions.route()
                .POST("/login", accept(MediaType.APPLICATION_JSON), handler::login)
                .build();
    }
}
