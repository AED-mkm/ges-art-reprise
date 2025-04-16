package com.gest.art.security.repository;

import com.gest.art.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    Optional<User> findByUsernameOrEmail(String userName, String email);

    Boolean existsByEmail(String email);
}
