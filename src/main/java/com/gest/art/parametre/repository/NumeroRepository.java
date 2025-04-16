package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.NumeroFacture;
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface NumeroRepository extends JpaRepository<NumeroFacture, String> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<NumeroFacture> findByMagasinIdAndYear(String magasinId, Integer year);
}
