package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.Entre;
import com.gest.art.parametre.entite.EntreProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntreProduitRepository extends JpaRepository<EntreProduit, String> {
}
