package com.jonathan.chat.user.repository;

import com.jonathan.chat.user.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    String findPasswordByUsername(String username);
    boolean existsAppUserByUsername(String username);
    Optional<AppUser> findByUsernameAndPassword(String username, String password);
}
