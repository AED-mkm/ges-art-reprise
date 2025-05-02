package com.gest.art.parametre.entite.dto;
import com.gest.art.parametre.entite.BonDeCmdeFour;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BonDeCmdeFourDTO extends AbstractAuditingEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	private LocalDate dateBonDeCmdeFour;
	private String libBonDeCmdeFour;
	private BigDecimal totalCmdeFour;

	// Références aux IDs des entités liées
	private String fournId;
	private String magId;
	private List<String> prodBonCmdeFourIds;

	public static BonDeCmdeFourDTO fromEntity(BonDeCmdeFour bonDeCmdeFour) {
		if (bonDeCmdeFour == null) {
			return null;
		}

		return BonDeCmdeFourDTO.builder()
				.id(bonDeCmdeFour.getId())
				.dateBonDeCmdeFour(bonDeCmdeFour.getDateBonDeCmdeFour())
				.libBonDeCmdeFour(bonDeCmdeFour.getLibBonDeCmdeFour())
				.totalCmdeFour(bonDeCmdeFour.getTotalCmdeFour())
				.fournId(bonDeCmdeFour.getFournisseur() != null ? bonDeCmdeFour.getFournisseur().getId() : null)
				.magId(bonDeCmdeFour.getMagasin() != null ? bonDeCmdeFour.getMagasin().getId() : null)
				.prodBonCmdeFourIds(bonDeCmdeFour.getProdBonCmdeFours() != null ?
						bonDeCmdeFour.getProdBonCmdeFours().stream()
								.map(prod -> prod.getId())
								.collect(Collectors.toList()) : null)
				/*.createdBy(bonDeCmdeFour.getCreatedBy())
				.lastModifiedBy(bonDeCmdeFour.getLastModifiedBy())
				.createdDate(bonDeCmdeFour.getCreatedDate())
				.lastModifiedDate(bonDeCmdeFour.getLastModifiedDate())*/
				.build();
	}

	public static BonDeCmdeFour toEntity(BonDeCmdeFourDTO dto) {
		if (dto == null) {
			return null;
		}

		return BonDeCmdeFour.builder()
				.id(dto.getId())
				.dateBonDeCmdeFour(dto.getDateBonDeCmdeFour())
				.libBonDeCmdeFour(dto.getLibBonDeCmdeFour())
				.totalCmdeFour(dto.getTotalCmdeFour() != null ? dto.getTotalCmdeFour() : BigDecimal.ZERO)
				// Les relations doivent être gérées séparément
				.build();
	}
}
