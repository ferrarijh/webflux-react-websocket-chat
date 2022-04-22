package com.jonathan.chat.gw.handler;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jonathan.chat.gw.dto.AppResponseBody;
import com.jonathan.chat.gw.dto.AuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppHandler {

    private final JWTVerifier verifier;

    public Mono<ServerResponse> handleInitialAuthentication(ServerRequest request){
        if(!request.cookies().containsKey("access_token"))
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();

        String submittedToken = Objects.requireNonNull(request.cookies().getFirst("access_token")).getValue().toString();
        try{
            DecodedJWT verifiedToken = verifier.verify(submittedToken);
            String username = verifiedToken.getSubject();
            AuthDto responseBody = new AuthDto(username);
            return ServerResponse.ok().bodyValue(responseBody);
        }catch(JWTVerificationException jve){
            AppResponseBody responseBody = new AppResponseBody(jve.getMessage());
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(responseBody);
        }
    }

    public Mono<ServerResponse> handleRemoveToken(ServerRequest request) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", "")
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("access_token", "")
                .maxAge(0)
                .build();

        return ServerResponse.ok()
                .cookie(accessTokenCookie)
                .cookie(refreshTokenCookie)
                .build();
    }
}
