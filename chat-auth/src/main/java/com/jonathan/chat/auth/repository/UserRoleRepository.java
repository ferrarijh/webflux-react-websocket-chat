package com.jonathan.chat.auth.repository;

import com.jonathan.chat.auth.entity.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<AppUserRole, Long> {
    AppUserRole findByName(String name);
}
