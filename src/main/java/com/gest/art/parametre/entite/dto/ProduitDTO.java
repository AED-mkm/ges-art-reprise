package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.Produit;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProduitDTO extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private Integer codeprod;
    private String libelle;

    @NotNull
    private BigDecimal prixActuel;

    private BigDecimal ancienPrix;
    private BigDecimal prixMax;
    private BigDecimal stockProduit;
    private int nbElement;
    private BigDecimal coutAchat;
    private BigDecimal ancienCoutAchat;
    private BigDecimal coutEmballage;
    private String typeEmballage;

    // Références aux IDs des entités liées
    private String magasinId;
    private List<String> prodBonCmdeFourIds;

    public static ProduitDTO fromEntity(Produit produit) {
        if (produit == null) {
            return null;
        }

        return ProduitDTO.builder()
                .id(produit.getId())
                .codeprod(produit.getCodeprod())
                .libelle(produit.getLibelle())
                .prixActuel(produit.getPrixActuel())
                .ancienPrix(produit.getAncienPrix())
                .prixMax(produit.getPrixMax())
                .stockProduit(produit.getStockProduit())
                .nbElement(produit.getNbElement())
                .coutAchat(produit.getCoutAchat())
                .ancienCoutAchat(produit.getAncienCoutAchat())
                .coutEmballage(produit.getCoutEmballage())
                .typeEmballage(produit.getTypeEmballage())
                .magasinId(produit.getMagasin() != null ? produit.getMagasin().getId() : null)
                .prodBonCmdeFourIds(produit.getProdBonCmdeFour() != null ?
                        produit.getProdBonCmdeFour().stream()
                                .map(prod -> prod.getId())
                                .collect(Collectors.toList()) : null)
               /* .createdBy(produit.getCreatedBy())
                .lastModifiedBy(produit.getLastModifiedBy())
                .createdDate(produit.getCreatedDate())
                .lastModifiedDate(produit.getLastModifiedDate())*/
                .build();
    }

    public static Produit toEntity(ProduitDTO dto) {
        if (dto == null) {
            return null;
        }

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
