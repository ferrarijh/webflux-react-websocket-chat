package com.jonathan.chat.user;

import com.jonathan.chat.user.entity.AppUser;
import com.jonathan.chat.user.entity.AppUserRole;
import com.jonathan.chat.user.service.AppUserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserApplication.class);
        String hostFromEnv = System.getenv("USER_DB_HOST");
        if(hostFromEnv != null)
            app.setDefaultProperties(Map.of("app.datasource.url", hostFromEnv));
        app.run(args);
    }

    @Bean
    public ApplicationRunner applicationRunner(AppUserService userService){
        return (args) -> {
            userService.addRole(new AppUserRole(null, "ROLE_USER"));
            userService.addRole(new AppUserRole(null, "ROLE_ADMIN"));
            userService.addRole(new AppUserRole(null, "ROLE_ADMIN_TRAINEE"));
        };
    }
}