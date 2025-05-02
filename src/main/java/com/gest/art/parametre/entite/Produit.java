package com.gest.art.parametre.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_produits")
@Cacheable(value = true)
@SQLDelete(sql = "UPDATE kg_produits SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Produit extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(unique = true, nullable = false)
    private Integer codeprod;
    private String libelle;
    @NotNull
    @Column(name = "prix_actuel")
    private BigDecimal prixActuel = BigDecimal.ZERO;
    @Column(name = "ancien_prix")
    private BigDecimal ancienPrix = BigDecimal.ZERO;
    @Column(name = "prix_Maxi")
    private BigDecimal prixMax = BigDecimal.ZERO; // prix maxi du produit à ne pas depasser
    @Column(name = "stock")
    private BigDecimal stockProduit =  BigDecimal.ZERO;
    @Column(name = "nb_element")
    private int nbElement;
    @Column(name = "cout_achat")
    private BigDecimal coutAchat = BigDecimal.ZERO;
    @Column(name = "ancien_cout_achat")
    private BigDecimal ancienCoutAchat = BigDecimal.ZERO;
    @Column(name = "cout_embal")
    private BigDecimal coutEmballage = BigDecimal.ZERO;
    @Column(name = "type_embal")
    private String typeEmballage;
    @JsonIgnore
    @OneToMany(mappedBy = "produit")
    private List<ProdBonCmdeFour> prodBonCmdeFour;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "produit", allowSetters = true)
    private Magasin magasin;

}
