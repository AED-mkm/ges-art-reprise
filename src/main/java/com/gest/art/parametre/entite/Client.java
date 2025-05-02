package com.gest.art.parametre.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@Table(name = "kg_client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_client set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class Client extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "code_client", unique = true)
    private String codeClient;
    @NotBlank(message = "le nom du client est obligatoire")
    @Column(name = "denomination")
    private String denomination;
    @Column(name = "contact")
    private String contactClient;
    @Column(name = "adresse")
    private String adresseClient;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeClient", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "client", allowSetters = true)
    private TypeClient typeClient;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "client", allowSetters = true)
    private Magasin magasin;
    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Vente> ventes;
    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Facture> factures;

}