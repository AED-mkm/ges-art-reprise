package com.gest.art.parametre.entite;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
import java.time.LocalDate;


/**
 * @author Moctar
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_stock_produit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_stock_produit set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class StockProduit extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "prix_max")
    private BigDecimal prixMax;

    @Column(name = "stock_produit")
    private BigDecimal stockProduit;

    @Column(name = "cout_achat")
    private BigDecimal coutAchat;

    @Column(name = "anc_cout_achat")
    private BigDecimal ancienCoutAchat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produit_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "stock_id", allowSetters = true)
    private Produit produit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "stock_id", allowSetters = true)
    private Magasin magasin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vente_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "stock_id", allowSetters = true)
    private Vente vente;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entre_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "stock_id", allowSetters = true)
    private Entre entre;

}