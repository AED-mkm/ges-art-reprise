package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.Facture;
import com.gest.art.parametre.entite.Magasin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<Facture, String> {

}
