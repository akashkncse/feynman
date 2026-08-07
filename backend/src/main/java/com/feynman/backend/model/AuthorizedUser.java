package com.feynman.backend.model;

import jakarta.persistence.*;

@Entity
public class AuthorizedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String role;
}
