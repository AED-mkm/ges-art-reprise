package com.gest.art.parametre.entite;



import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
import java.math.BigDecimal;
import java.util.List;


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
@Table(name = "kg_taxe")
@Cacheable(value = true)
@SQLDelete(sql = "UPDATE kg_taxe SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Taxe extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @NotBlank(message = "le code de la taxe est obligatoire")
    @Column(name = "code", unique = true)
    private String code;
    @NotBlank(message = "le libelle de la taxe est obligatoire")
    @Column(name = "libelle", unique = true)
    private String libelle;
    @NotNull
    @Column(name = "taxe")
    private BigDecimal taxe;
    @OneToMany(mappedBy = "taxe")
    private List<Vente> ventes;

}