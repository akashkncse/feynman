package com.feynman.backend.repository;

import com.feynman.backend.model.AuthorizedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizedUserRepository extends JpaRepository<AuthorizedUser, Long> {

    Optional<AuthorizedUser> findByEmail(String email);
}
