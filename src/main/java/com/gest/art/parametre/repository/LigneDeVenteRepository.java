package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.parametre.entite.LigneDeVente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneDeVenteRepository extends JpaRepository<LigneDeVente, String> {
}
