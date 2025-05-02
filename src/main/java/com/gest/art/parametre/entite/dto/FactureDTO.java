package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.Facture;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactureDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String numFacture;
	private LocalDate dateFacture;

	// Références aux IDs des entités liées
	private String magasinId;
	private String clientId;

	public static FactureDTO fromEntity(Facture facture) {
		if (facture == null) {
			return null;
		}

		return FactureDTO.builder()
				.id(facture.getId())
				.numFacture(facture.getNumFacture())
				.dateFacture(facture.getDateFacture())
				.magasinId(facture.getMagasin() != null ? facture.getMagasin().getId() : null)
				.clientId(facture.getClient() != null ? facture.getClient().getId() : null)
				/*.createdBy(facture.getCreatedBy())
				.lastModifiedBy(facture.getLastModifiedBy())
				.createdDate(facture.getCreatedDate())
				.lastModifiedDate(facture.getLastModifiedDate())*/
				.build();
	}

	public static Facture toEntity(FactureDTO dto) {
		if (dto == null) {
			return null;
		}

		return Facture.builder()
				.id(dto.getId())
				.numFacture(dto.getNumFacture())
				.dateFacture(dto.getDateFacture())
				// Les relations doivent être gérées séparément
				.build();
	}
}