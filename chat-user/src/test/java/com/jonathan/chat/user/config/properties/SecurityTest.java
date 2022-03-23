package com.jonathan.chat.user.config.properties;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = AppProperties.class)
public class SecurityTest {

    @Autowired
    AppProperties props;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void BCrypt_always_returns_different_string(){
        String randomUUID = UUID.randomUUID().toString();
        String pw1 = passwordEncoder.encode(randomUUID);
        String pw2 = passwordEncoder.encode(randomUUID);
        Assertions.assertNotEquals(pw1, pw2);
    }

    @Test
    void verifyJWT(){
        Algorithm algorithm = Algorithm.HMAC256(props.getSecret());
        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        String token = JWT.create().withClaim("jiho", "USER")
                .withExpiresAt(new Date(System.currentTimeMillis()+30000))
                .sign(algorithm);

        try{
            verifier.verify(token);

            fail();
        }catch (JWTVerificationException e){
            System.out.println("passed :)");
            e.printStackTrace();
        }


    }

}
