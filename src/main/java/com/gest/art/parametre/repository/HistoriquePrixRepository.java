package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.Fournisseur;
import com.gest.art.parametre.entite.historique.HistoriquePrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriquePrixRepository extends JpaRepository<HistoriquePrix, String> {
}
