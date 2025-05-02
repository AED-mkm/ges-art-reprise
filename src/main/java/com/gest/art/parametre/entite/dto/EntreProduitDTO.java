package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntreProduitDTO extends AbstractAuditingEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	@Positive
	private BigDecimal quantite;
	@Positive
	private BigDecimal prixEntre;

	// Références aux IDs des entités liées
	private String entreId;
	@NotBlank
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