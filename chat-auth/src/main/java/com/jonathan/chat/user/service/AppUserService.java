package com.jonathan.chat.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jonathan.chat.user.dto.JoinForm;
import com.jonathan.chat.user.dto.TokenPair;
import com.jonathan.chat.user.entity.AppUser;
import com.jonathan.chat.user.config.properties.AppProperties;
import com.jonathan.chat.user.entity.AppUserRole;
import com.jonathan.chat.user.repository.UserRepository;
import com.jonathan.chat.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Algorithm hmac256;
    private final AppProperties props;

    public String registerUser(JoinForm form){
        if(form.getUsername().isBlank() || form.getPassword().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "Field must not be empty");

        AppUser user = new AppUser();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.getRoles().add(userRoleRepository.findByName("ROLE_USER"));
        userRepository.save(user);

        return "Successfully registered new user.";
    }

    public TokenPair authenticate(AppUser principal){
        if(principal.getUsername().isBlank() || principal.getPassword().isBlank())
            throw new ResponseStatusException(BAD_REQUEST, "Field must not be empty");

        if(!userRepository.existsByUsername(principal.getUsername()))
            throw new ResponseStatusException(NOT_FOUND, "Username does not exist.");

        String hashedPassword = passwordEncoder.encode(principal.getPassword());
        Optional<AppUser> user = userRepository.findByUsernameAndPassword(principal.getUsername(), hashedPassword);

        if(user.isEmpty())
            throw new ResponseStatusException(UNAUTHORIZED);

        long now = System.currentTimeMillis();
        String accessToken = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(now + props.getAccessTokenDuration() * 60 * 1000))
                .withClaim("roles", user.get().getRoles())
                .sign(hmac256);

        String refreshToken = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(now + props.getAccessTokenDuration()* 60 * 1000))
                .sign(hmac256);
        return new TokenPair(accessToken, refreshToken);
    }

    public AppUser getUser(String id){
        var optionalId = userRepository.findById(Long.parseLong(id));
        if(optionalId.isEmpty())
            throw new ResponseStatusException(NOT_FOUND, "Username does not exist.");
        return optionalId.get();
    }

    public AppUserRole addRole(AppUserRole role){
        return userRoleRepository.save(role);
    }
}
