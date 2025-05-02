package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Operation;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String codeOp;
	private LocalDate dateOP;
	private double montantOp;
	private String sensOp;
	private String observationOp;

	// Références aux IDs des entités liées
	private String succursaleId;
	private String magasinId;

	public static OperationDTO fromEntity(Operation operation) {
		if (operation == null) {
			return null;
		}

		return OperationDTO.builder()
				.id(operation.getId())
				.codeOp(operation.getCodeOp())
				.dateOP(operation.getDateOP())
				.montantOp(operation.getMontantOp())
				.sensOp(operation.getSensOp())
				.observationOp(operation.getObservationOp())
				.succursaleId(operation.getSuccursale() != null ? operation.getSuccursale().getId() : null)
				.magasinId(operation.getMagasin() != null ? operation.getMagasin().getId() : null)
				/*.createdBy(operation.getCreatedBy())
				.lastModifiedBy(operation.getLastModifiedBy())
				.createdDate(operation.getCreatedDate())
				.lastModifiedDate(operation.getLastModifiedDate())*/
				.build();
	}

	public static Operation toEntity(OperationDTO dto) {
		if (dto == null) {
			return null;
		}

		return Operation.builder()
				.id(dto.getId())
				.codeOp(dto.getCodeOp())
				.dateOP(dto.getDateOP())
				.montantOp(dto.getMontantOp())
				.sensOp(dto.getSensOp())
				.observationOp(dto.getObservationOp())
				// Les relations doivent être gérées séparément
				.build();
	}
}