package com.jonathan.chat.auth;

import com.jonathan.chat.auth.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService userService;

    @PostMapping("/user/join")
    ResponseEntity<AppUser> registerUser(AppUser user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/chat/user/join").toUriString());
        return ResponseEntity.created(uri).body(userService.registerUser(user));
    }

    @GetMapping("/user/{id}")
    ResponseEntity<AppUser> getUser(@PathVariable("id") String id){
        return ResponseEntity.ok().body(userService.getUser(id));
    }

}
