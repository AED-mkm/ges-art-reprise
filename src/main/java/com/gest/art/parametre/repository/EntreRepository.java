package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.Entre;
import com.gest.art.parametre.entite.Magasin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntreRepository extends JpaRepository<Entre, String> {
}
