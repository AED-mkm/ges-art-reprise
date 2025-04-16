package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, String> {
}
