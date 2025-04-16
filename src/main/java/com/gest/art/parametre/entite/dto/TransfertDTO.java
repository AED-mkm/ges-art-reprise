package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.Transfert;
import com.gest.art.parametre.entite.TransfertProduit;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransfertDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	@NotNull(message = "Le magasin source est obligatoire")
	private String magasinSourceId;

	@NotNull(message = "Le magasin destination est obligatoire")
	private String magasinDestinationId;

	private LocalDateTime dateTransfert;
	private String motifTransfert;

	// Only include IDs of related transfertProduits to avoid circular references
	private List<String> transfertProduitsIds;

	// Mapping from Entity to DTO
	public static TransfertDTO fromEntity(Transfert transfert) {
		if (transfert == null) {
			return null;
		}

		return TransfertDTO.builder()
				.id(transfert.getId())
				.magasinSourceId(transfert.getMagasinSource() != null ?
						transfert.getMagasinSource().getId() : null)
				.magasinDestinationId(transfert.getMagasinDestination() != null ?
						transfert.getMagasinDestination().getId() : null)
				.dateTransfert(transfert.getDateTransfert())
				.motifTransfert(transfert.getMotifTransfert())
				.transfertProduitsIds(transfert.getTransfertProduits() != null ?
						transfert.getTransfertProduits().stream()
								.map(TransfertProduit::getId)
								.collect(Collectors.toList()) : null)
				.build();
	}

	// Mapping from DTO to Entity
	public static Transfert toEntity(TransfertDTO dto) {
		if (dto == null) {
			return null;
		}

		return Transfert.builder()
				.id(dto.getId())
				.dateTransfert(dto.getDateTransfert())
				.motifTransfert(dto.getMotifTransfert())
				// Note: relationships should be handled separately
				.build();
	}
}