package com.jonathan.chat.gw.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class SecurityComponent {

    private final AppProperties props;

    @Bean
    JWTVerifier jwtVerifier(Algorithm algorithm){
        return JWT.require(algorithm)
                .withIssuer("chat-user-service/chat/user/signin")
                .build();
    }

    @Bean
    Algorithm algorithm(){
        return Algorithm.HMAC256(props.getSecret());
    }
}