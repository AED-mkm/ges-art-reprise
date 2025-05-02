package com.gest.art.parametre.entite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Cacheable;
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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;


/**
 * @author Moctar
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_prod_bord_liv")
@Cacheable(value = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
@SQLDelete(sql = "UPDATE kg_prod_bord_liv SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class ProduitBordLiv extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "qte_bord_liv")
    private int qteBordLiv;
    @Column(name = "prix_bord_liv")
    private double prixBordLiv;
    @Column(name = "prix_achat_bord")
    private double prixAchatBordLiv;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bord_liv_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "prod_bord_liv", allowSetters = true)
    private BordereauLivraison bordereauLivraison;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "prod_bord_liv", allowSetters = true)
    private Magasin magasin;

}