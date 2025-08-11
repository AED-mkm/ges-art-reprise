package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.parametre.entite.Facture;
import com.gest.art.parametre.entite.NumeroBordereau;
import com.gest.art.parametre.entite.NumeroFacture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BordLivNumeroRepository extends JpaRepository<NumeroBordereau, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<NumeroBordereau> findByMagasinIdAndYear(String magasinId, Integer year);

}
