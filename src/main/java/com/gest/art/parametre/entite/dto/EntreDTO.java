package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Entre;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntreDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private LocalDate dateEnt = LocalDate.now();
	private String objet;
	private String numBordLiv;

	// Références aux IDs des entités liées
	private String fournisseurId;
	private String magasinId;
	private List<EntreProduitDTO> entreProduits = new ArrayList<>();

	public static EntreDTO fromEntity(Entre entre) {
		if (entre == null) {
			return null;
		}

		return EntreDTO.builder()
				.id(entre.getId())
				.dateEnt(entre.getDateEnt())
				.objet(entre.getObjet())
				.numBordLiv(entre.getNumBordLiv())
				.fournisseurId(entre.getFournisseur() != null ? entre.getFournisseur().getId() : null)
				.magasinId(entre.getMagasin() != null ? entre.getMagasin().getId() : null)
				.entreProduits(entre.getEntreProduits() != null ?
						entre.getEntreProduits().stream()
								.map(ep -> EntreProduitDTO.fromEntity(ep)) // Mapping de EntreProduit vers EntreProduitDTO
								.collect(Collectors.toList()) : null)
				/*.createdBy(entre.getCreatedBy())
				.lastModifiedBy(entre.getLastModifiedBy())
				.createdDate(entre.getCreatedDate())
				.lastModifiedDate(entre.getLastModifiedDate())*/
				.build();
	}


	public static Entre toEntity(EntreDTO dto) {
		if (dto == null) {
			return null;
		}
		return Entre.builder()
				.id(dto.getId())
				.dateEnt(dto.getDateEnt())
				.objet(dto.getObjet())
				.numBordLiv(dto.getNumBordLiv())
				// Les relations doivent être gérées séparément
				.build();
	}
}
