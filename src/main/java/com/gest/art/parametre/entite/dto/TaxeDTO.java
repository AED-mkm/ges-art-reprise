package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.Taxe;
import com.gest.art.parametre.entite.Vente;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxeDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	@NotBlank(message = "Le code de la taxe est obligatoire")
	private String code;

	@NotBlank(message = "Le libellé de la taxe est obligatoire")
	private String libelle;

	@NotNull(message = "Le taux de taxe est obligatoire")
	private BigDecimal taxe;

	// Only include IDs of related ventes to avoid circular references
	private List<String> ventesIds;

	// Mapping from Entity to DTO
	public static TaxeDTO fromEntity(Taxe taxe) {
		if (taxe == null) {
			return null;
		}

		return TaxeDTO.builder()
				.id(taxe.getId())
				.code(taxe.getCode())
				.libelle(taxe.getLibelle())
				.taxe(taxe.getTaxe())
				.ventesIds(taxe.getVentes() != null ?
						taxe.getVentes().stream()
								.map(Vente::getId)
								.collect(Collectors.toList()) : null)
				.build();
	}

	// Mapping from DTO to Entity
	public static Taxe toEntity(TaxeDTO dto) {
		if (dto == null) {
			return null;
		}

		return Taxe.builder()
				.id(dto.getId())
				.code(dto.getCode())
				.libelle(dto.getLibelle())
				.taxe(dto.getTaxe())
				// Note: ventes relationship should be handled separately
				.build();
	}
}