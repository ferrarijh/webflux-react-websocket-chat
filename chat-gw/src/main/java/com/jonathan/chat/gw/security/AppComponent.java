package com.jonathan.chat.gw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.beans.JavaBean;

@Component
public class AppComponent {

    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
