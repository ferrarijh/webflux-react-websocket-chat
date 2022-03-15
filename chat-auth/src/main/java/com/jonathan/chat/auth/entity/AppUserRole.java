package com.jonathan.chat.auth.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
public class AppUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;
}