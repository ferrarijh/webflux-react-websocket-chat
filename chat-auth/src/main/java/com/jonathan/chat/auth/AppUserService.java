package com.jonathan.chat.auth;

import com.jonathan.chat.auth.entity.AppUser;
import com.jonathan.chat.auth.repository.UserRepository;
import com.jonathan.chat.auth.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    AppUser registerUser(AppUser user){
//        user.getRoles().add(roleRepository.findByName("ROLE_USER"));
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    AppUser getUser(String id){
        var optionalId = userRepository.findById(Long.parseLong(id));
        if(optionalId.isEmpty())
            throw new ResponseStatusException(NOT_FOUND);
        return optionalId.get();
    }
}
