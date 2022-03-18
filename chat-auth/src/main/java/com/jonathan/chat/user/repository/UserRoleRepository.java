package com.jonathan.chat.user.repository;

import com.jonathan.chat.user.entity.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<AppUserRole, Long> {
    AppUserRole findByName(String name);
}
