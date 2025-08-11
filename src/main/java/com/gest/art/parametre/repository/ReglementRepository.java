package com.gest.art.parametre.repository;



import com.gest.art.parametre.entite.Reglement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReglementRepository extends JpaRepository<Reglement, String> {
	List<Reglement> findByBordereauId(String bordereauId);

	@Query("SELECT COALESCE(SUM(r.montantRegl), 0) FROM Reglement r WHERE r.bordereau.id = :bordereauId")
	Optional<BigDecimal> sumByBordereauId(@Param("bordereauId") String bordereauId);


}
