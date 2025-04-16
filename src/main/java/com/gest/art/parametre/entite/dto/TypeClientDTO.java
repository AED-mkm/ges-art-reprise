package com.gest.art.parametre.entite.dto;
import com.gest.art.parametre.entite.TypeClient;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeClientDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	@NotBlank(message = "Le code du type client est obligatoire")
	private String code;

	@NotBlank(message = "Le libellé du type client est obligatoire")
	private String libelle;

	// Only store ID to avoid circular references
	private String magasinId;

	// Only store IDs of related clients
	private List<String> clientsIds;

	// Mapping from Entity to DTO
	public static TypeClientDTO fromEntity(TypeClient typeClient) {
		if (typeClient == null) {
			return null;
		}

		return TypeClientDTO.builder()
				.id(typeClient.getId())
				.code(typeClient.getCode())
				.libelle(typeClient.getLibelle())
				.magasinId(typeClient.getMagasin() != null ?
						typeClient.getMagasin().getId() : null)
				.clientsIds(typeClient.getClients() != null ?
						typeClient.getClients().stream()
								.map(client -> client.getId())
								.toList() : null)
				.build();
	}

	// Mapping from DTO to Entity
	public static TypeClient toEntity(TypeClientDTO dto) {
		if (dto == null) {
			return null;
		}

		return TypeClient.builder()
				.id(dto.getId())
				.code(dto.getCode())
				.libelle(dto.getLibelle())
				// Relationships should be handled separately
				.build();
	}
}