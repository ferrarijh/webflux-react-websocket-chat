package com.jonathan.chat.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jonathan.chat.user.dto.CustomResponse;
import com.jonathan.chat.user.dto.TokenPair;
import com.jonathan.chat.user.entity.AppUser;
import com.jonathan.chat.user.config.properties.AppProperties;
import com.jonathan.chat.user.entity.AppUserRole;
import com.jonathan.chat.user.repository.UserRepository;
import com.jonathan.chat.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Algorithm hmac256;
    private final AppProperties props;

    @Transactional
    public CustomResponse registerUser(AppUser form){
        if(form.getUsername().isBlank() || form.getPassword().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "Field must not be empty");

        if(userRepository.existsAppUserByUsername(form.getUsername()))
            throw new ResponseStatusException(CONFLICT, String.format("Username %s already exists.", form.getUsername()));

        AppUser user = new AppUser();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRoles(userRoleRepository.findAllByName(props.getRoleUser()));
        userRepository.save(user);

        return new CustomResponse("Successfully registered new user.");
    }

    public TokenPair authenticate(AppUser principal){
        if(principal.getUsername().isBlank() || principal.getPassword().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "Field must not be empty");

        Optional<AppUser> user = userRepository.findByUsername(principal.getUsername());
        if(user.isEmpty())
            throw new ResponseStatusException(NOT_FOUND, "Username does not exist.");

        if(!passwordEncoder.matches(principal.getPassword(), user.get().getPassword()))
            throw new ResponseStatusException(UNAUTHORIZED, "Incorrect password.");

        long now = System.currentTimeMillis();
        String accessToken = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(now + props.getAccessTokenDuration() * 60 * 1000))
                .withIssuer("chat-user-service/chat/user/signin")
                .withClaim("roles",
                        user.get().getRoles().stream()
                                .map(AppUserRole::getName)
                                .collect(Collectors.toList())
                ).sign(hmac256);

        String refreshToken = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(now + props.getAccessTokenDuration()* 60 * 1000))
                .withIssuer("chat-user-service/chat/user/signin")
                .sign(hmac256);
        return new TokenPair(accessToken, refreshToken);
    }

    public AppUser getUser(String id){
        var optionalId = userRepository.findById(Long.parseLong(id));
        if(optionalId.isEmpty())
            throw new ResponseStatusException(NOT_FOUND, "Username does not exist.");
        return optionalId.get();
    }

    /**
     * Only authorized requests should invoke this method. Unauthorized requests should be filtered at the gateway.
     */
    @Transactional
    public void deleteUser(AppUser principal){
        var optionalUser = userRepository.findByUsername(principal.getUsername());
        if(optionalUser.isEmpty())
            throw new ResponseStatusException(NOT_FOUND, "Username does not exist.");

        userRepository.delete(optionalUser.get());
    }

    public AppUserRole addRole(AppUserRole role){
        return userRoleRepository.save(role);
    }
}
