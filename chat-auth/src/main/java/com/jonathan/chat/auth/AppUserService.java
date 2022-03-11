package com.jonathan.chat.auth;

import com.jonathan.chat.auth.entity.AppUser;
import com.jonathan.chat.auth.repository.UserRepository;
import com.jonathan.chat.auth.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
