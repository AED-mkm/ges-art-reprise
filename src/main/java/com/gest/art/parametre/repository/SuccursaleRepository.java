package com.gest.art.parametre.repository;


import com.gest.art.parametre.entite.Succursale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuccursaleRepository extends JpaRepository<Succursale, String> {
	Optional <Succursale> findSuccursaleById(String id);
}
