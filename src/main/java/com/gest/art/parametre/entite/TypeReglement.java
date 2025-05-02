package com.gest.art.parametre.entite;



import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "kg_type_reglt")
@Cacheable(value = true)
@SQLDelete(sql = "UPDATE kg_type_reglt SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class TypeReglement extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @NotBlank(message = "le code de type client est obligatoire")
    @Column(name = "code",unique = true)
    private String code;
    @NotBlank(message = "le de type reglement est obligatoire")
    @Column(name = "type_regl", unique = true)
    private String typeRegl;
}