package com.jonathan.chat.user.api;

import com.jonathan.chat.user.dto.CustomResponse;
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
    ResponseEntity<CustomResponse> join(
            HttpServletRequest request,
            @RequestBody AppUser joinForm
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
        Cookie atCookie = new Cookie("access_token", tokens.getAccessToken());
        Cookie rtCookie = new Cookie("refresh_token", tokens.getRefreshToken());
        atCookie.setHttpOnly(true);
        rtCookie.setHttpOnly(true);
        response.addCookie(atCookie);
        response.addCookie(rtCookie);
        return ResponseEntity.created(URI.create(request.getRequestURI())).build();
    }

    @DeleteMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteUser(
            @RequestBody AppUser user
    ){
        userService.deleteUser(user);
        return ResponseEntity.ok().body(String.format("Successfully deleted user [ %s ]", user.getUsername()));
    }

    /* Only for logging */
    @GetMapping(path = "/signout")
    ResponseEntity<String> signOut(){
        return ResponseEntity.ok().body("signout");
    }
}
