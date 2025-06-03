package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.Operation;
import com.gest.art.parametre.entite.Succursale;
import com.gest.art.parametre.entite.enums.SensOp;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
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
	private BigDecimal montantOp = BigDecimal.ZERO;
	private SensOp sensOp;
	private String observationOp;

	// Références aux IDs des entités liées
	private SuccursaleDTO succursaleDTO;
	private String magasinId;

	public static OperationDTO fromEntity(Operation operation) {
		if (operation == null) {
			return null;
		}

		OperationDTO operationDTO = new OperationDTO();
		operationDTO.setId(operation.getId());
		operationDTO.setCodeOp(operation.getCodeOp());
		operationDTO.setDateOP(operation.getDateOP());
		operationDTO.setMontantOp(operation.getMontantOp());
		operationDTO.setSensOp(operation.getSensOp());
		operationDTO.setObservationOp(operation.getObservationOp());
		operationDTO.setSuccursaleDTO(SuccursaleDTO.fromEntity(operation.getSuccursale()));
		return operationDTO;
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
				.succursale(SuccursaleDTO.toEntity(dto.getSuccursaleDTO()))
				// Les relations doivent être gérées séparément
				.build();
	}
}