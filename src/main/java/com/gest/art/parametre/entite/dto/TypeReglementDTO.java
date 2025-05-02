package com.gest.art.parametre.entite.dto;
import com.gest.art.parametre.entite.TypeReglement;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeReglementDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	@NotBlank(message = "Le code du type de règlement est obligatoire")
	@Size(max = 20, message = "Le code ne doit pas dépasser 20 caractères")
	private String code;

	@NotBlank(message = "Le type de règlement est obligatoire")
	@Size(max = 50, message = "Le type de règlement ne doit pas dépasser 50 caractères")
	private String typeRegl;

	// Mapping from Entity to DTO
	public static TypeReglementDTO fromEntity(TypeReglement typeReglement) {
		if (typeReglement == null) {
			return null;
		}

		return TypeReglementDTO.builder()
				.id(typeReglement.getId())
				.code(typeReglement.getCode())
				.typeRegl(typeReglement.getTypeRegl())
				/*.createdBy(typeReglement.getCreatedBy())
				.lastModifiedBy(typeReglement.getLastModifiedBy())
				.createdDate(typeReglement.getCreatedDate())
				.lastModifiedDate(typeReglement.getLastModifiedDate())*/
				.build();
	}

	// Mapping from DTO to Entity
	public static TypeReglement toEntity(TypeReglementDTO dto) {
		if (dto == null) {
			return null;
		}

		return TypeReglement.builder()
				.id(dto.getId())
				.code(dto.getCode())
				.typeRegl(dto.getTypeRegl())
				.build();
	}
}
