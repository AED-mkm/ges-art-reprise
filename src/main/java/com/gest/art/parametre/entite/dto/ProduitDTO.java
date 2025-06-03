package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.Produit;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
public class ProduitDTO extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private Integer codeprod;
    private String libelle;
    @NotNull
    private BigDecimal prixActuel = BigDecimal.ZERO;
    private BigDecimal ancienPrix;
    private BigDecimal prixMax;
    private BigDecimal stockProduit;
    private int nbElement;
    private BigDecimal coutAchat;
    private BigDecimal ancienCoutAchat;
    private BigDecimal coutEmballage;
    private String typeEmballage;

    // Références aux IDs des entités liées
    /* private String magasinId;
    private List<String> prodBonCmdeFourIds;*/

    public static ProduitDTO fromEntity(Produit produit) {
        if (produit == null) {
            return null;
        }

        ProduitDTO produitDTO = new ProduitDTO();
        produitDTO.setId(produit.getId());
        produitDTO.setCodeprod(produit.getCodeprod());
        produitDTO.setLibelle(produit.getLibelle());
        produitDTO.setPrixActuel(produit.getPrixActuel());
        produitDTO.setAncienPrix(produit.getAncienPrix());
        produitDTO.setPrixMax(produit.getPrixMax());
        produitDTO.setCoutEmballage(produit.getCoutEmballage());
        produitDTO.setTypeEmballage(produit.getTypeEmballage());
        return produitDTO;
    }

    public static Produit toEntity(ProduitDTO dto) {
        return Produit.builder()
                .id(dto.getId())
                .codeprod(dto.getCodeprod())
                .libelle(dto.getLibelle())
                .prixActuel(dto.getPrixActuel() != null ? dto.getPrixActuel() : BigDecimal.ZERO)
                .ancienPrix(dto.getAncienPrix() != null ? dto.getAncienPrix() : BigDecimal.ZERO)
                .prixMax(dto.getPrixMax() != null ? dto.getPrixMax() : BigDecimal.ZERO)
                .stockProduit(dto.getStockProduit() != null ? dto.getStockProduit() : BigDecimal.ZERO)
                .nbElement(dto.getNbElement())
                .coutAchat(dto.getCoutAchat() != null ? dto.getCoutAchat() : BigDecimal.ZERO)
                .ancienCoutAchat(dto.getAncienCoutAchat() != null ? dto.getAncienCoutAchat() : BigDecimal.ZERO)
                .coutEmballage(dto.getCoutEmballage() != null ? dto.getCoutEmballage() : BigDecimal.ZERO)
                .typeEmballage(dto.getTypeEmballage())
                // Les relations doivent être gérées séparément
                .build();
    }
}
