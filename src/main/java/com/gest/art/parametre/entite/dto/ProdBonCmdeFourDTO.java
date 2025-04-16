package com.gest.art.parametre.entite.dto;



import com.gest.art.parametre.entite.ProdBonCmdeFour;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProdBonCmdeFourDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private BigDecimal qteProdCmde;
	private BigDecimal prixProdCmde;
	private BigDecimal montantProdCmde;

	// Références aux IDs des entités liées
	private String produitId;
	private String bonDeCmdeFourId;
	private String magasinId;

	public static ProdBonCmdeFourDTO fromEntity(ProdBonCmdeFour prodBonCmdeFour) {
		if (prodBonCmdeFour == null) {
			return null;
		}

		return ProdBonCmdeFourDTO.builder()
				.id(prodBonCmdeFour.getId())
				.qteProdCmde(prodBonCmdeFour.getQteProdCmde())
				.prixProdCmde(prodBonCmdeFour.getPrixProdCmde())
				.montantProdCmde(prodBonCmdeFour.getMontantProdCmde())
				.produitId(prodBonCmdeFour.getProduit() != null ? prodBonCmdeFour.getProduit().getId() : null)
				.bonDeCmdeFourId(prodBonCmdeFour.getBonDeCmdeFour() != null ? prodBonCmdeFour.getBonDeCmdeFour().getId() : null)
				.magasinId(prodBonCmdeFour.getMagasin() != null ? prodBonCmdeFour.getMagasin().getId() : null)
				/*.createdBy(prodBonCmdeFour.getCreatedBy())
				.lastModifiedBy(prodBonCmdeFour.getLastModifiedBy())
				.createdDate(prodBonCmdeFour.getCreatedDate())
				.lastModifiedDate(prodBonCmdeFour.getLastModifiedDate())*/
				.build();
	}

	public static ProdBonCmdeFour toEntity(ProdBonCmdeFourDTO dto) {
		if (dto == null) {
			return null;
		}

		return ProdBonCmdeFour.builder()
				.id(dto.getId())
				.qteProdCmde(dto.getQteProdCmde() != null ? dto.getQteProdCmde() : BigDecimal.ZERO)
				.prixProdCmde(dto.getPrixProdCmde() != null ? dto.getPrixProdCmde() : BigDecimal.ZERO)
				.montantProdCmde(dto.getMontantProdCmde() != null ? dto.getMontantProdCmde() : BigDecimal.ZERO)
				// Les relations doivent être gérées séparément
				.build();
	}
}