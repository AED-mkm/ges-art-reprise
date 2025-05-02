package com.gest.art.parametre.entite.dto;



import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.Operation;
import com.gest.art.parametre.entite.Succursale;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuccursaleDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String codeSucc;

	@NotBlank(message = "le libelle de la succ est obligatoire")
	private String libelleSucc;

	private String contactSucc;


	// Références aux IDs des entités liées
	private String banqueId;
	private List<String> operationsIds;

	public static SuccursaleDTO fromEntity(Succursale succursale) {
		if (succursale == null) {
			return null;
		}

		return SuccursaleDTO.builder()
				.id(succursale.getId())
				.codeSucc(succursale.getCodeSucc())
				.libelleSucc(succursale.getLibelleSucc())
				.contactSucc(succursale.getContactSucc())
				.banqueId(succursale.getBanque() != null ? succursale.getBanque().getId() : null)
				.operationsIds(
						Optional.ofNullable(succursale.getOperations())
								.orElse(List.of())
								.stream()
								.map(Operation::getId)
								.collect(Collectors.toList())
				)
				/*.createdBy(succursale.getCreatedBy())
			    .lastModifiedBy(succursale.getLastModifiedBy())
			    .createdDate(succursale.getCreatedDate())
			    .lastModifiedDate(succursale.getLastModifiedDate())*/
				.build();
	}

	public static Succursale toEntity(SuccursaleDTO dto) {
		if (dto == null) {
			return null;
		}

		return Succursale.builder()
				.id(dto.getId())
				.codeSucc(dto.getCodeSucc())
				.libelleSucc(dto.getLibelleSucc())
				.contactSucc(dto.getContactSucc())
				.banque(dto.getBanqueId() != null ? Banque.builder().id(dto.getBanqueId()).build() : null)
				.operations(
						Optional.ofNullable(dto.getOperationsIds())
								.orElse(List.of())
								.stream()
								.map(id -> Operation.builder().id(id).build())
								.collect(Collectors.toList())
				)
				.build();
	}

}