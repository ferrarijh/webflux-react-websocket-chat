package com.jonathan.chat.user.api;

import com.jonathan.chat.user.config.properties.AppProperties;
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
    private final AppProperties props;

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

//        atCookie.setMaxAge(props.getAccessTokenDuration() * 60);
        atCookie.setMaxAge(-1);
        atCookie.setHttpOnly(true);
        atCookie.setPath("/");
        atCookie.setDomain("127.0.0.1");
//        rtCookie.setMaxAge(props.getRefreshTokenDuration() * 86400);
        rtCookie.setMaxAge(-1);
        rtCookie.setHttpOnly(true);
        rtCookie.setPath("/");
        rtCookie.setDomain("127.0.0.1");

        response.addCookie(atCookie);
        response.addCookie(rtCookie);

        return ResponseEntity.created(URI.create(request.getRequestURI())).build();
    }

    @DeleteMapping(path = "/delete", consumes = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> deleteUser(
            @RequestBody AppUser user
    ){
        userService.deleteUser(user);
        return ResponseEntity.ok().body(String.format("Successfully deleted user [ %s ]", user.getUsername()));
    }

    /* For test */
    @PostMapping(path = "/signout", consumes = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> signOut(HttpServletResponse response){
        Cookie atCookie = new Cookie("access_token", null);
        Cookie rtCookie = new Cookie("refresh_token", null);

        atCookie.setMaxAge(0);
        rtCookie.setMaxAge(0);

        response.addCookie(atCookie);
        response.addCookie(rtCookie);

        return ResponseEntity.ok().body("Successfully logged out.");
    }

    /* For logging */
    @GetMapping(path = "/signout")
    ResponseEntity<String> signOut(){
        return ResponseEntity.ok().body("signout");
    }
}
