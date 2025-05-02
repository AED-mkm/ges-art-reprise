package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.*;
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
public class AvoirDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private LocalDate dateAvoir;
	private double qteAvoir;
	private int qteLiv;
	private String etatLiv;
	private LocalDate dateLiv;
	private String numAvoir;

	// Références aux IDs des entités liées
	private String factureId;
	private String bordereauLivraisonId;
	private String magasinId;

	public static AvoirDTO fromEntity(Avoir avoir) {
		if (avoir == null) return null;

		return AvoirDTO.builder()
				.id(avoir.getId())
				.dateAvoir(avoir.getDateAvoir())
				.qteAvoir(avoir.getQteAvoir())
				.qteLiv(avoir.getQteLiv())
				.etatLiv(avoir.getEtatLiv())
				.dateLiv(avoir.getDateLiv())
				.numAvoir(avoir.getNumAvoir())
				.factureId(avoir.getFacture() != null ? avoir.getFacture().getId() : null)
				.bordereauLivraisonId(avoir.getBordereauLivraison() != null ? avoir.getBordereauLivraison().getId() : null)
				.magasinId(avoir.getMagasin() != null ? avoir.getMagasin().getId() : null)
				/*.createdBy(avoir.getCreatedBy())
				.lastModifiedBy(avoir.getLastModifiedBy())
				.createdDate(avoir.getCreatedDate())
				.lastModifiedDate(avoir.getLastModifiedDate())*/
				.build();
	}

	public static Avoir toEntity(AvoirDTO dto) {
		if (dto == null) return null;

		return Avoir.builder()
				.id(dto.getId())
				.dateAvoir(dto.getDateAvoir())
				.qteAvoir(dto.getQteAvoir())
				.qteLiv(dto.getQteLiv())
				.etatLiv(dto.getEtatLiv())
				.dateLiv(dto.getDateLiv())
				.numAvoir(dto.getNumAvoir())
				// Les relations doivent être gérées séparément
				.build();
	}
}
