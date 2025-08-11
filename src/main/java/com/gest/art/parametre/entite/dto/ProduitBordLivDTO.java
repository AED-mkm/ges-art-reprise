package com.gest.art.parametre.entite.dto;



import com.gest.art.parametre.entite.ProduitBordLiv;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProduitBordLivDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private BigDecimal qteBordLiv;
	private BigDecimal prixBordLiv;
	private BigDecimal prixAchatBordLiv;
	//reference avec produit
	private String produitId;
	private Integer codeprod;
	private String libelle;
	private ProduitDTO produitDTO;
	private BigDecimal stockProduit;

	// Références aux IDs des entités liées
	//private String bordereauLivraisonId;
	//private String magasinId;

	public static ProduitBordLivDTO fromEntity(ProduitBordLiv produitBordLiv) {
		if (produitBordLiv == null) {
			return null;
		}

		ProduitBordLivDTO produitBordLivDTO = new ProduitBordLivDTO();
		produitBordLivDTO.setId(produitBordLiv.getId());
		produitBordLivDTO.setQteBordLiv(produitBordLiv.getQteBordLiv());
		produitBordLivDTO.setPrixBordLiv(produitBordLiv.getPrixBordLiv());
		produitBordLivDTO.setPrixAchatBordLiv(produitBordLiv.getPrixAchatBordLiv());
		produitBordLivDTO.setProduitDTO(ProduitDTO.fromEntity(produitBordLiv.getProduit()));
		produitBordLivDTO.setStockProduit(produitBordLiv.getProduit().getStockProduit());
		return produitBordLivDTO;
	}

	public static ProduitBordLiv toEntity(ProduitBordLivDTO dto) {
		if (dto == null) {
			return null;
		}

		return ProduitBordLiv.builder()
				.id(dto.getId())
				.qteBordLiv(dto.getQteBordLiv())
				.prixBordLiv(dto.getPrixBordLiv())
				.prixAchatBordLiv(dto.getPrixAchatBordLiv())
				// Les relations doivent être gérées séparément
				.build();
	}
}