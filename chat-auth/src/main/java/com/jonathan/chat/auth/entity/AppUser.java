package com.jonathan.chat.auth.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<AppUserRole> roles;
}
