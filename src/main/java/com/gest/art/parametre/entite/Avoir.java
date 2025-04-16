package com.gest.art.parametre.entite;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
import java.time.LocalDate;


/**
 * @author Moctar
 */
@SuppressWarnings("ALL")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_avoir")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_avoir set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class Avoir extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "date_avoir")
    private LocalDate dateAvoir;

    @Column(name = "quantite_avoir")
    private double qteAvoir;

    @Column(name = "quantite_livrer")
    private int qteLiv;

    @Column(name = "etat_livr")
    private String etatLiv;

    @Column(name = "date_livr")
    private LocalDate dateLiv;

    @Column(name = "num_avoir")
    private String numAvoir;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "avoirs", allowSetters = true)
    private Facture facture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bordLiv_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "avoirs", allowSetters = true)
    private BordereauLivraison bordereauLivraison;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "avoir", allowSetters = true)
    private Magasin magasin;

}