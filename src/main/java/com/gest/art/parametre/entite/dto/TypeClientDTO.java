package com.gest.art.parametre.entite.dto;
import com.gest.art.parametre.entite.TypeClient;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeClientDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	@NotBlank(message = "Le code du type client est obligatoire")
	private String code;
	@NotBlank(message = "Le libellé du type client est obligatoire")
	private String libelle;

	public static TypeClientDTO fromEntity(TypeClient typeClient) {
		if (typeClient == null) {
			return null;
		}
		TypeClientDTO typeClientDTO = new TypeClientDTO();
		typeClientDTO.setId(typeClient.getId() );
		typeClientDTO.setCode(typeClient.getCode());
		typeClientDTO.setLibelle(typeClient.getLibelle());
		return typeClientDTO;
	}

	public static TypeClient toEntity(TypeClientDTO typeClientDTO) {
		return TypeClient.builder()
				.id(typeClientDTO.getId())
				.code(typeClientDTO.getCode())
				.libelle(typeClientDTO.getLibelle())
				// Relationships should be handled separately
				.build();
	}
}