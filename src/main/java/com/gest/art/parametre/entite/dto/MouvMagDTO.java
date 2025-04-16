package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.MouvMag;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MouvMagDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String numvehicule;
	private String nomChauffeur;
	private String numBordereau;
	private String sens;
	private String typeBlf;
	private LocalDate dateMouv;
	private String provenance;

	// Références aux IDs des entités liées
	private String factureId;
	private String bordereauLivraisonId;
	private String magasinId;

	public static MouvMagDTO fromEntity(MouvMag mouvMag) {
		if (mouvMag == null) {
			return null;
		}

		return MouvMagDTO.builder()
				.id(mouvMag.getId())
				.numvehicule(mouvMag.getNumvehicule())
				.nomChauffeur(mouvMag.getNomChauffeur())
				.numBordereau(mouvMag.getNumBordereau())
				.sens(mouvMag.getSens())
				.typeBlf(mouvMag.getTypeBlf())
				.dateMouv(mouvMag.getDateMouv())
				.provenance(mouvMag.getProvenance())
				.factureId(mouvMag.getFacture() != null ? mouvMag.getFacture().getId() : null)
				.bordereauLivraisonId(mouvMag.getBordereauLivraison() != null ? mouvMag.getBordereauLivraison().getId() : null)
				.magasinId(mouvMag.getMagasin() != null ? mouvMag.getMagasin().getId() : null)
			/*	.createdBy(mouvMag.getCreatedBy())
				.lastModifiedBy(mouvMag.getLastModifiedBy())
				.createdDate(mouvMag.getCreatedDate())
				.lastModifiedDate(mouvMag.getLastModifiedDate())*/
				.build();
	}

	public static MouvMag toEntity(MouvMagDTO dto) {
		if (dto == null) {
			return null;
		}

		return MouvMag.builder()
				.id(dto.getId())
				.numvehicule(dto.getNumvehicule())
				.nomChauffeur(dto.getNomChauffeur())
				.numBordereau(dto.getNumBordereau())
				.sens(dto.getSens())
				.typeBlf(dto.getTypeBlf())
				.dateMouv(dto.getDateMouv())
				.provenance(dto.getProvenance())
				// Les relations doivent être gérées séparément
				.build();
	}
}