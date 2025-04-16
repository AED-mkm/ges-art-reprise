package com.gest.art.parametre.entite.dto;



import com.gest.art.parametre.entite.ProduitBordLiv;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProduitBordLivDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private int qteBordLiv;
	private double prixBordLiv;
	private double prixAchatBordLiv;

	// Références aux IDs des entités liées
	private String bordereauLivraisonId;
	private String magasinId;

	public static ProduitBordLivDTO fromEntity(ProduitBordLiv produitBordLiv) {
		if (produitBordLiv == null) {
			return null;
		}

		return ProduitBordLivDTO.builder()
				.id(produitBordLiv.getId())
				.qteBordLiv(produitBordLiv.getQteBordLiv())
				.prixBordLiv(produitBordLiv.getPrixBordLiv())
				.prixAchatBordLiv(produitBordLiv.getPrixAchatBordLiv())
				.bordereauLivraisonId(produitBordLiv.getBordereauLivraison() != null ?
						produitBordLiv.getBordereauLivraison().getId() : null)
				.magasinId(produitBordLiv.getMagasin() != null ?
						produitBordLiv.getMagasin().getId() : null)
				/*.createdBy(produitBordLiv.getCreatedBy())
				.lastModifiedBy(produitBordLiv.getLastModifiedBy())
				.createdDate(produitBordLiv.getCreatedDate())
				.lastModifiedDate(produitBordLiv.getLastModifiedDate())*/
				.build();
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