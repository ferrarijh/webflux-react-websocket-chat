package com.jonathan.chat.user.api;

import com.jonathan.chat.user.dto.JoinForm;
import com.jonathan.chat.user.dto.TokenPair;
import com.jonathan.chat.user.entity.AppUser;
import com.jonathan.chat.user.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@RestController
@RequestMapping(path = "/chat/user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;

    @PostMapping(path = "/join", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> join(
            HttpServletRequest request,
            @RequestBody JoinForm joinForm
    ){
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(userService.registerUser(joinForm));
    }

    @PostMapping(path = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> signIn(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AppUser principal
    ) {
        TokenPair tokens = userService.authenticate(principal);
        response.addCookie(new Cookie("access_token", tokens.getAccessToken()));
        response.addCookie(new Cookie("refresh_token", tokens.getRefreshToken()));
        return ResponseEntity.created(URI.create(request.getRequestURI())).build();
    }

    /* Only for logging */
    @GetMapping(path = "/signout")
    ResponseEntity<String> signOut(){
        return ResponseEntity.ok().build();
    }
}
