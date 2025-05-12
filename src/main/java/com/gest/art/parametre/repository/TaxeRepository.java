package com.gest.art.parametre.repository;



import com.gest.art.parametre.entite.Taxe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaxeRepository extends JpaRepository<Taxe, String> {
 Optional<Taxe> findTaxeById(String id);
 List<Taxe> findByCodeIn(List<String> codes);
}
