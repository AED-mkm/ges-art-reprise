package com.gest.art.parametre.repository;


import com.gest.art.parametre.entite.Banque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BanqueRepository extends JpaRepository<Banque, String> {
	Optional <Banque> findBanqueById(String id);
}
