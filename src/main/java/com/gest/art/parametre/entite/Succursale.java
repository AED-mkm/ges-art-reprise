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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@SuppressWarnings("common-java:DuplicatedBlocks")
@SQLDelete(sql = "UPDATE kg_succ SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Succursale extends AbstractAuditingEntity {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "code_succ", unique = true)
    private String codeSucc;
    @NotBlank(message = "le libelle de la succ est obligatoire")
    @Column(name = "lib_succ")
    private String libelleSucc;
    @Column(name = "contact_succ")
    private String contactSucc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "banque", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "succursales", allowSetters = true)
    private Banque banque;

    @OneToMany(mappedBy = "succursale")
    private List<Operation> operations;

}