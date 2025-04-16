package com.gest.art.parametre.entite;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.parametre.entite.enums.TypeVente;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "kg_vente")
@Cacheable(value = true)
@SQLDelete(sql = "UPDATE kg_vente SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Vente extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "date_vte")
    private LocalDate dateVente;
    @Column(name = "type_vte")
    @Enumerated(EnumType.STRING)
    private TypeVente typeVente;
    @Column(name = "objet")
    private String objet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "magasin", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "vente", allowSetters = true)
    private Magasin magasin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "vente", allowSetters = true)
    private Client client;
    @OneToMany(mappedBy = "vente")
    private List<LigneDeVente> lignesDeVente;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxe", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "vente", allowSetters = true)
    private Taxe taxe;
    @Column(name = "montant_ht")
    private BigDecimal montantHt = BigDecimal.ZERO;
    @Column(name = "montant_tva")
    private BigDecimal montantTva = BigDecimal.ZERO;
    @Column(name = "montant_bic")
    private BigDecimal montantBic = BigDecimal.ZERO;
    @Column(name = "montant_ttc")
    private BigDecimal montantTTC = BigDecimal.ZERO;
    private Long factureId;
}