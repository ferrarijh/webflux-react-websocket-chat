package com.jonathan.chat.user.config.properties;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = AppProperties.class)
@TestPropertySource("classpath:test.properties")
public class SecurityTest {

    @Autowired
    AppProperties props;

    PasswordEncoder passwordEncoder;
    Algorithm hmac256;
    JWTVerifier verifier;

    @BeforeEach
    void init(){
        passwordEncoder = new BCryptPasswordEncoder();
        hmac256 = Algorithm.HMAC256(props.getSecret());
        verifier = JWT.require(hmac256).build();
    }

    @Test
    void BCrypt_always_returns_different_string(){
        String randomUUID = UUID.randomUUID().toString();
        String pw1 = passwordEncoder.encode(randomUUID);
        String pw2 = passwordEncoder.encode(randomUUID);
        Assertions.assertNotEquals(pw1, pw2);
    }

    @Test
    void test_verifier(){
        String jwtSample;
        DecodedJWT decodedJwtSample;

        Date in30s = new Date(System.currentTimeMillis()+30_000);
        String subject = "jiho";
        List<String> roles = Arrays.asList("ADMIN", "USER");
        jwtSample = JWT.create()
                .withSubject("jiho")
                .withClaim("roles", roles)
                .withExpiresAt(in30s)
                .sign(hmac256);

        decodedJwtSample = verifier.verify(jwtSample);
    }

}
