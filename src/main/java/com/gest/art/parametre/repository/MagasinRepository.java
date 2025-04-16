package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.Magasin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MagasinRepository extends JpaRepository<Magasin, String> {
    Magasin findMagasinById(String id);
}
