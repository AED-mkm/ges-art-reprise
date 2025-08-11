package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.dto.BordereauLivraisonDTO;
import com.gest.art.parametre.entite.dto.DetailsBordereauDTO;
import com.gest.art.parametre.entite.enums.EtatBordereau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BordereauRepository extends JpaRepository<BordereauLivraison, String> {
	List<DetailsBordereauDTO>findByClientIdAndEtatBordereau(String clientId, EtatBordereau etatBordereau);
	List<BordereauLivraison>getByClientIdAndEtatBordereau(String clientId,EtatBordereau etatBordereau);
	boolean existsByIdAndClientId(String bordereauId, String clientId);

	@Query("SELECT COALESCE(SUM(b.netApayer - COALESCE((SELECT SUM(r.montantRegl) FROM Reglement r WHERE r.bordereau.id = b.id), 0)), 0) " +
			"FROM BordereauLivraison b WHERE b.id IN :bordereauIds")
	BigDecimal calculerTotalImpayer(@Param("bordereauIds") List<BordereauLivraison> bordereauIds);



}
