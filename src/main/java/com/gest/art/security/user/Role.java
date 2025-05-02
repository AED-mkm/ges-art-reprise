package com.gest.art.security.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_roles")
@Cacheable(value = true)
@SQLDelete(sql = "UPDATE kg_roles SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Role extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @NotBlank(message = "obligatoire")
    @Column(unique = true)
    private String name;
    private String description;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}
