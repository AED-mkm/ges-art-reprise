package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Reglement;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReglementDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String numRegl;
	private LocalDate dateRegl;
	private double montantRegl;
	private String libelleRegl;
	private String typeRegl;

	// Références aux IDs des entités liées
	private String bonDeCmdeFourId;
	private String magasinId;

	public static ReglementDTO fromEntity(Reglement reglement) {
		if (reglement == null) {
			return null;
		}

		return ReglementDTO.builder()
				.id(reglement.getId())
				.numRegl(reglement.getNumRegl())
				.dateRegl(reglement.getDateRegl())
				.montantRegl(reglement.getMontantRegl())
				.libelleRegl(reglement.getLibelleRegl())
				.typeRegl(reglement.getTypeRegl())
				.bonDeCmdeFourId(reglement.getBonDeCmdeFour() != null ? reglement.getBonDeCmdeFour().getId() : null)
				.magasinId(reglement.getMagasin() != null ? reglement.getMagasin().getId() : null)
				/*.createdBy(reglement.getCreatedBy())
				.lastModifiedBy(reglement.getLastModifiedBy())
				.createdDate(reglement.getCreatedDate())
				.lastModifiedDate(reglement.getLastModifiedDate())*/
				.build();
	}

	public static Reglement toEntity(ReglementDTO dto) {
		if (dto == null) {
			return null;
		}

		return Reglement.builder()
				.id(dto.getId())
				.numRegl(dto.getNumRegl())
				.dateRegl(dto.getDateRegl())
				.montantRegl(dto.getMontantRegl())
				.libelleRegl(dto.getLibelleRegl())
				.typeRegl(dto.getTypeRegl())
				// Les relations doivent être gérées séparément
				.build();
	}
}
