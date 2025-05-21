package com.gest.art.parametre.entite;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Moctar
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_bordereau_liv")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_bordereau_liv set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class BordereauLivraison extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "num_bord")
    private String numBordereau;

    @Column(name = "date_bord")
    private LocalDate dateBordereau;

/*    @Column(name = "total_bord")
    private BigDecimal totalBordereau;*/

   /* @Column(name = "total_general")
    private BigDecimal totalGeneral;*/

    /*@Column(name = "total_remise")
    private BigDecimal totalRemise;*/

    @Column(name = "net_a_payer")
    private BigDecimal netApayer;

    @Column(name = "total_transport")
    private BigDecimal totalTransport;

    @Column(name = "total_emballage")
    private BigDecimal totalEmballage;

    @Column(name = "montant_payer")
    private BigDecimal montantPayer;

    @Column(name = "taux_tva")
    private BigDecimal tauxTva;

    @Column(name = "etat_bord")
    private String etatBordereau;

    @Column(name = "montant_tva")
    private BigDecimal montantTva;

    @Column(name = "taux_bic")
    private BigDecimal tauxBic;

    @Column(name = "montant_bic")
    private BigDecimal montantBic;

    @Column(name = "montant_ttc")
    private BigDecimal montantTtc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "bord_liv", allowSetters = true)
    private Client client;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "bord_liv", allowSetters = true)
    private Magasin magasin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxe", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "vente", allowSetters = true)
    private Taxe taxe;

}