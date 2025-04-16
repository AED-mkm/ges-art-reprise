package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password, String> {
}
