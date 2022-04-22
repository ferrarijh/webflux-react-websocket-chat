package com.jonathan.chat.gw.filter;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.chat.gw.dto.AppResponseBody;
import com.jonathan.chat.gw.security.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

//TODO("Figure out why using @RequiredArgsConstructor throws exception"
//@RequiredArgsConstructor

@Component
@Slf4j
public class GWAuthFilter extends AbstractGatewayFilterFactory<GWAuthFilter.Config> {

    private final JWTVerifier verifier;
    private final AppProperties props;
    private final ObjectMapper mapper;

    @Autowired
    public GWAuthFilter(JWTVerifier verifier, AppProperties props, ObjectMapper mapper){
        super(Config.class);
        this.verifier = verifier;
        this.props = props;
        this.mapper = mapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            /* check if the request contains access token */
            if(!request.getCookies().containsKey("access_token"))
                return writeResponse(
                        response,
                        HttpStatus.UNAUTHORIZED,
                        "Access token is required to access this path."
                );

            String principal = Objects.requireNonNull(request.getCookies().getFirst("access_token")).getValue();
            try{
                /* verify token */
                DecodedJWT decodedJWT = verifier.verify(principal);

                List<String> claims = decodedJWT.getClaim("roles").asList(String.class);
                if(!claims.contains(props.getRoleUser()))
                    return writeResponse(
                            response,
                            HttpStatus.FORBIDDEN,
                            "No permission"
                    );

                return chain.filter(exchange);

            }catch(JWTVerificationException jve){
                jve.printStackTrace();
                return writeResponse(
                        response,
                        HttpStatus.UNAUTHORIZED,
                        "Invalid token"
                );
            }
        };
    }

    /**
     * Write http response with a particular status code as a part of the header and and message as its body.
     */
    private Mono<Void> writeResponse(ServerHttpResponse response, HttpStatus status, String msg){
        Flux<DataBuffer> bodyFlux;
        try{
            byte[] bodyBytes = mapper.writeValueAsBytes(new AppResponseBody(msg));
            response.setStatusCode(status);
            DataBuffer buffer = response.bufferFactory().wrap(bodyBytes);
            bodyFlux = Flux.just(buffer);
        }catch (JsonProcessingException jpe){
            jpe.printStackTrace();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            bodyFlux = Flux.empty();
        }
        return response.writeWith(bodyFlux);
    }

    public static class Config{}
}
