package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.Entre;
import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.dto.EntreProduitDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntreProduitRepository extends JpaRepository<EntreProduit, String> {
 List<EntreProduit> findByEntreId(String entreId);
}
