package com.jonathan.chat.gw.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AppCorsFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        int pathElementsNum = request.getPath().elements().size();
        String lastPathElement = request.getPath().elements().get(pathElementsNum-1).value();

        if(lastPathElement.equals("validate-token")) {
            HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
            responseHeaders.setAccessControlAllowOrigin("http://127.0.0.1:3000");
            responseHeaders.setAccessControlAllowCredentials(true);
        }

        return chain.filter(exchange);
    }
}
