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
@Table(name = "kg_operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_operation set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class Operation extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "code_op")
    private String codeOp;
    @Column(name = "date_op")
    private LocalDate dateOP;
    @Column(name = "montant_op")
    private double montantOp;
    @Column(name = "sens_op")
    private String sensOp;
    @Column(name = "observ_op")
    private String observationOp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "succ_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "operation", allowSetters = true)
    private Succursale succursale;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "operation", allowSetters = true)
    private Magasin magasin;

}