package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;

import java.math.BigDecimal;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EntreProduitDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private BigDecimal quantite;
	private BigDecimal prixEntre;

	// Références aux IDs des entités liées
	private String entreId;
	private String produitId;

	public static EntreProduitDTO fromEntity(EntreProduit entreProduit) {
		if (entreProduit == null) {
			return null;
		}

		return EntreProduitDTO.builder()
				.id(entreProduit.getId())
				.quantite(entreProduit.getQuantite())
				.prixEntre(entreProduit.getPrixEntre())
				.entreId(entreProduit.getEntre() != null ? entreProduit.getEntre().getId() : null)
				.produitId(entreProduit.getProduit() != null ? entreProduit.getProduit().getId() : null)
				/*.createdBy(entreProduit.getCreatedBy())
				.lastModifiedBy(entreProduit.getLastModifiedBy())
				.createdDate(entreProduit.getCreatedDate())
				.lastModifiedDate(entreProduit.getLastModifiedDate())*/
				.build();
	}

	public static EntreProduit toEntity(EntreProduitDTO dto) {
		if (dto == null) {
			return null;
		}

		return EntreProduit.builder()
				.id(dto.getId())
				.quantite(dto.getQuantite())
				.prixEntre(dto.getPrixEntre())
				// Les relations doivent être gérées séparément
				.build();
	}
}