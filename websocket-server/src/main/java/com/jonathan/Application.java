package com.jonathan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class Application {
    @PostConstruct
    public void onStart(){
        TimeZone.setDefault(TimeZone.getDefault());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
