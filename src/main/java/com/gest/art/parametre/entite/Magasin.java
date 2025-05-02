package com.gest.art.parametre.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import com.gest.art.security.user.User;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_magasin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_magasin set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class Magasin extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "kg_code_magasin")
    private String codeMagasin;
    @NotBlank(message = "le nom du magasin est obligatoire")
    @Column(name = "kg_libelle")
    private String nomMagasin;
    @Column(name = "adresse_mag")
    private String adresseMagasin;
    @Column(name = "contact_mag")
    private String contactMagasin;
    @Column(name = "responsable")
    private String responsableMag;
    @JsonIgnore
    @OneToMany(mappedBy = "magasin")
    private List<Produit> produits;
    @JsonIgnore
    @OneToMany(mappedBy = "magasin")
    private List<Fournisseur> fournisseurs;
    @JsonIgnore
    @OneToMany(mappedBy = "magasin")
    private List<Vente> ventes;
    @JsonIgnore
    @OneToMany(mappedBy = "magasin")
    private List<User> users;
}
