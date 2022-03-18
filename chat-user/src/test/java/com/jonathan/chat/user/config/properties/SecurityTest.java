package com.jonathan.chat.user.config.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class SecurityTest {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void BCrypt_always_returns_different_string(){
        String randomUUID = UUID.randomUUID().toString();
        String pw1 = passwordEncoder.encode(randomUUID);
        String pw2 = passwordEncoder.encode(randomUUID);
        Assertions.assertNotEquals(pw1, pw2);
    }

}
