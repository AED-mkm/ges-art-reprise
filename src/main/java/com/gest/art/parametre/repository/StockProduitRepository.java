package com.gest.art.parametre.repository;


import com.gest.art.parametre.entite.StockProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StockProduitRepository extends JpaRepository<StockProduit, String> {
	Optional<StockProduit> findByProduitId(String produitId);
}
