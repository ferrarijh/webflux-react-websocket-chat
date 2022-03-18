package com.jonathan.chat.user.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.jonathan.chat.user.config.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppSecurityComponent {

    private final AppProperties props;
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    JWTVerifier jwtVerifier(Algorithm algorithm){
        return JWT.require(algorithm).build();
    }

    @Bean
    Algorithm algorithm(){
        return Algorithm.HMAC256(props.getSecret());
    }
}
