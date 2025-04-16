package com.gest.art.parametre.repository;



import com.gest.art.parametre.entite.Taxe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxeRepository extends JpaRepository<Taxe, String> {
 Optional<Taxe> findTaxeById(String id);
}
