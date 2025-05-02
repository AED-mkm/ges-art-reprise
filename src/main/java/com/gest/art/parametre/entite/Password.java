package com.gest.art.parametre.entite;

import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_passwords")
@Cacheable(value = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
@SQLDelete(sql = "UPDATE kg_passwords SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Password extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(unique = true, nullable = false)
    private String libelle;
    private boolean etat;

}
