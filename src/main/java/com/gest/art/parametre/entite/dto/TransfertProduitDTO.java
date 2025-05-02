package com.gest.art.parametre.entite.dto;
import com.gest.art.parametre.entite.TransfertProduit;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransfertProduitDTO extends AbstractAuditingEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;

	@NotNull(message = "L'ID du transfert est obligatoire")
	private String transfertId;

	@NotNull(message = "L'ID du produit est obligatoire")
	private String produitId;

	@NotNull(message = "La quantité est obligatoire")
	@Positive(message = "La quantité doit être positive")
	private BigDecimal quantiteTransfert;

	// Mapping from Entity to DTO
	public static TransfertProduitDTO fromEntity(TransfertProduit transfertProduit) {
		if (transfertProduit == null) {
			return null;
		}

		return TransfertProduitDTO.builder()
				.id(transfertProduit.getId())
				.transfertId(transfertProduit.getTransfert() != null ?
						transfertProduit.getTransfert().getId() : null)
				.produitId(transfertProduit.getProduit() != null ?
						transfertProduit.getProduit().getId() : null)
				.quantiteTransfert(transfertProduit.getQuantiteTransfert())
				/*.createdBy(transfertProduit.getCreatedBy())
				.lastModifiedBy(transfertProduit.getLastModifiedBy())
				.createdDate(transfertProduit.getCreatedDate())
				.lastModifiedDate(transfertProduit.getLastModifiedDate())*/
				.build();
	}

	// Mapping from DTO to Entity
	public static TransfertProduit toEntity(TransfertProduitDTO dto) {
		if (dto == null) {
			return null;
		}

		return TransfertProduit.builder()
				.id(dto.getId())
				.quantiteTransfert(dto.getQuantiteTransfert())
				// Note: relationships should be handled separately
				.build();
	}
}