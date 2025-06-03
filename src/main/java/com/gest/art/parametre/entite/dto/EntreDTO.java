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
	private String nomFour;

	//private FournisseurDTO fournisseurDTO;
	private String magasinId;
	private String nomMagasin;
	//private MagasinDTO magasinDTO;
	private List<EntreProduitDTO> entreProduits ;

	public static EntreDTO fromEntity(Entre entre) {
		if (entre == null) {
			return null;
		}

	EntreDTO entreDTO = new EntreDTO();
	entreDTO.setId( entre.getId());
	entreDTO.setObjet(entre.getObjet());
	entreDTO.setDateEnt(entre.getDateEnt());
	entreDTO.setNumBordLiv(entre.getNumBordLiv());
	entreDTO.setFournisseurId(entre.getFournisseur().getId());
	entreDTO.setNomFour(entre.getFournisseur().getNomFour());
	entreDTO.setMagasinId(entre.getMagasin().getId());
	entreDTO.setNomMagasin(entre.getMagasin().getNomMagasin());
	return entreDTO;
	}


	public static Entre toEntity(EntreDTO dto) {
		return Entre.builder()
				.id(dto.getId())
				.dateEnt(dto.getDateEnt())
				.objet(dto.getObjet())
				.numBordLiv(dto.getNumBordLiv())
			/*	.fournisseur(FournisseurDTO.toEntity(dto.getFournisseurDTO()))
				.magasin(MagasinDTO.toEntity(dto.getMagasinDTO()))*/
				// Les relations doivent être gérées séparément
				.build();
	}
}
