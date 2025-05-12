package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LigneDeVenteDTO  extends AbstractAuditingEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;

	private String produitId;

	@Positive
	private BigDecimal qteVente = BigDecimal.ZERO;

	@Positive
	private BigDecimal prixUnitaire = BigDecimal.ZERO;

	private BigDecimal prixTotal = BigDecimal.ZERO;

	// Référence à l'ID de la vente
	private String venteId;

	public static LigneDeVenteDTO fromEntity(LigneDeVente ligneDeVente) {
		if (ligneDeVente == null) {
			return null;
		}

		return LigneDeVenteDTO.builder()
				.id(ligneDeVente.getId())
				.qteVente(ligneDeVente.getQteVente())
				.prixUnitaire(ligneDeVente.getPrixUnitaire())
				.prixTotal(ligneDeVente.getPrixTotal())
				.venteId(ligneDeVente.getVente() != null ? ligneDeVente.getVente().getId() : null)
				.build();
	}

	public static LigneDeVente toEntity(LigneDeVenteDTO dto) {
		if (dto == null) {
			return null;
		}

		return LigneDeVente.builder()
				.id(dto.getId())
				.qteVente(dto.getQteVente())
				.prixUnitaire(dto.getPrixUnitaire())
				.prixTotal(dto.getPrixTotal())
				// La relation vente doit être gérée séparément
				.build();
	}
}