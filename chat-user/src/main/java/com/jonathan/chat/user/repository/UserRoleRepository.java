package com.jonathan.chat.user.repository;

import com.jonathan.chat.user.entity.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<AppUserRole, Long> {
    List<AppUserRole> findAllByName(String name);
    AppUserRole findByName(String name);
}
