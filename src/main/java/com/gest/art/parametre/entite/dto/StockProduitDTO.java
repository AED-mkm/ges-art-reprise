package com.gest.art.parametre.entite.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.StockProduit;
import com.gest.art.parametre.entite.Vente;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @author Moctar
 */


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockProduitDTO extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private BigDecimal prixMax = BigDecimal.ZERO;;
    private BigDecimal stockProduit = BigDecimal.ZERO;;
    private BigDecimal coutAchat = BigDecimal.ZERO;;
    private BigDecimal ancienCoutAchat = BigDecimal.ZERO;
    private String produitId;
    private Integer codeprod;
    private String libelle;
  /*  private ProduitDTO produitDTO;
    private MagasinDTO magasinDTO;*/
    private String magasinId;
    private String codeMagasin;
    private String nomMagasin;
    /*private String venteId;
    private String entreId;*/

    public static StockProduitDTO fromEntity(StockProduit stockProduit) {
        if (stockProduit == null) {
            return null;
        }
        StockProduitDTO stockProduitDTO = new StockProduitDTO();
        stockProduitDTO.setId(stockProduit.getId());
        stockProduitDTO.setStockProduit(stockProduit.getStockProduit());
        stockProduitDTO.setPrixMax(stockProduit.getPrixMax());
        stockProduitDTO.setCoutAchat(stockProduit.getCoutAchat());
        stockProduitDTO.setAncienCoutAchat(stockProduit.getAncienCoutAchat());
        stockProduitDTO.setProduitId(stockProduit.getProduit().getId());
        stockProduitDTO.setCodeprod(stockProduit.getProduit().getCodeprod());
        stockProduitDTO.setLibelle(stockProduit.getProduit().getLibelle());
        stockProduitDTO.setMagasinId(stockProduit.getMagasin().getId());
        stockProduitDTO.setCodeMagasin(stockProduit.getMagasin().getCodeMagasin());
        stockProduitDTO.setNomMagasin(stockProduit.getMagasin().getNomMagasin());
        return stockProduitDTO;
    }

    public static StockProduit toEntity(StockProduitDTO dto) {
        return StockProduit.builder()
                .id(dto.getId())
                .stockProduit(dto.stockProduit)
                .prixMax(dto.getPrixMax()!=null?dto.getPrixMax():BigDecimal.ZERO)
                .coutAchat(dto.getCoutAchat()!=null?dto.getCoutAchat():BigDecimal.ZERO)
                .ancienCoutAchat(dto.getAncienCoutAchat()!=null?dto.getAncienCoutAchat():BigDecimal.ZERO)
                // Les relations doivent être gérées séparément
                .build();
    }

}