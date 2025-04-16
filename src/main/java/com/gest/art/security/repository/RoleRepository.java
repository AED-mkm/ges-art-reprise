package com.gest.art.security.repository;

import com.gest.art.security.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
    List<Role> findByName(String name);
}
