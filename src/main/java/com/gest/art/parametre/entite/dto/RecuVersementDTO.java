package com.gest.art.parametre.entite.dto;



import com.gest.art.parametre.entite.RecuVersement;
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
public class RecuVersementDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String numRecu;
	private LocalDate dateRecu;
	private double montantRecu;
	private String libelleRecu;
	private double tauxTva;
	private double montantTva;
	private double tauxBic;
	private double montantBic;
	private double montantTtc;

	// Références aux IDs des entités liées
	private String clientId;
	private String magasinId;

	public static RecuVersementDTO fromEntity(RecuVersement recuVersement) {
		if (recuVersement == null) {
			return null;
		}

		return RecuVersementDTO.builder()
				.id(recuVersement.getId())
				.numRecu(recuVersement.getNumRecu())
				.dateRecu(recuVersement.getDateRecu())
				.montantRecu(recuVersement.getMontantRecu())
				.libelleRecu(recuVersement.getLibelleRecu())
				.tauxTva(recuVersement.getTauxTva())
				.montantTva(recuVersement.getMontantTva())
				.tauxBic(recuVersement.getTauxBic())
				.montantBic(recuVersement.getMontantBic())
				.montantTtc(recuVersement.getMontantTtc())
				.clientId(recuVersement.getClient() != null ? recuVersement.getClient().getId() : null)
				.magasinId(recuVersement.getMagasin() != null ? recuVersement.getMagasin().getId() : null)
				/*.createdBy(recuVersement.getCreatedBy())
				.lastModifiedBy(recuVersement.getLastModifiedBy())
				.createdDate(recuVersement.getCreatedDate())
				.lastModifiedDate(recuVersement.getLastModifiedDate())*/
				.build();
	}

	public static RecuVersement toEntity(RecuVersementDTO dto) {
		if (dto == null) {
			return null;
		}

		return RecuVersement.builder()
				.id(dto.getId())
				.numRecu(dto.getNumRecu())
				.dateRecu(dto.getDateRecu())
				.montantRecu(dto.getMontantRecu())
				.libelleRecu(dto.getLibelleRecu())
				.tauxTva(dto.getTauxTva())
				.montantTva(dto.getMontantTva())
				.tauxBic(dto.getTauxBic())
				.montantBic(dto.getMontantBic())
				.montantTtc(dto.getMontantTtc())
				// Les relations doivent être gérées séparément
				.build();
	}
}
