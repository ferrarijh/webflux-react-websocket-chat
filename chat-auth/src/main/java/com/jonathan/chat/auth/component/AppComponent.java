package com.jonathan.chat.auth.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AppComponent {
    @Bean
    ObjectMapper mapper(){
        return new ObjectMapper();
    }
}
