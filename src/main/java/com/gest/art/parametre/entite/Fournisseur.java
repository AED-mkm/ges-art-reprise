package com.gest.art.parametre.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_fournisseur")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_fournisseur set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class Fournisseur extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "code_fourn", unique = true)
    private String codeFour;
    @NotBlank(message = "le nom du fournisseur est obligatoire")
    @Column(name = "nom_four")
    private String nomFour;
    @Column(name = "adresse_four")
    private String adresseFour;
    @Column(name = "contact_fourn")
    private String contactFour;
    @JsonIgnore
    @OneToMany(mappedBy = "fournisseur")
    private List<BonDeCmdeFour> bonDeCmdeFours;
    @JsonIgnore
    @OneToMany(mappedBy = "fournisseur")
    private List<Entre> entres;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "magasin", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "fournisseur", allowSetters = true)
    private Magasin magasin;

}

