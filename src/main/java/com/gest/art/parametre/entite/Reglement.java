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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
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
@Table(name = "kg_reglement")
@Cacheable(value = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
@SQLDelete(sql = "UPDATE kg_reglement SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Reglement extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "num_regl")
    private String numRegl;
    @Column(name = "date_regl")
    private LocalDate dateRegl;
    @Column(name = "montant_regl")
    private double montantRegl;
    @Column(name = "lib_regl")
    private String libelleRegl;
    @Column(name = "type_regl")
    private String typeRegl;
    /*@OneToMany(mappedBy = "reglment")
    private List<BordereauLivraison> bordereauLivraisons;*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bon_cmd_fpur", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "reglement", allowSetters = true)
    private BonDeCmdeFour bonDeCmdeFour;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "reglement", allowSetters = true)
    private Magasin magasin;

}