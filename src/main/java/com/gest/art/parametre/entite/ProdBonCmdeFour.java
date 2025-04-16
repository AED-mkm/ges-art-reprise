package com.gest.art.parametre.entite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.io.Serializable;
import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_prod_bon_cmd_four")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_prod_bon_cmd_four set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class ProdBonCmdeFour extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "qte_prod_cmde")
    private BigDecimal qteProdCmde = BigDecimal.ZERO;
    @Column(name = "prix_prod_cmde")
    private BigDecimal prixProdCmde = BigDecimal.ZERO;
    @Column(name = "mnt_prod_cmde")
    private BigDecimal montantProdCmde = BigDecimal.ZERO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "prod_bon_cmd_four", allowSetters = true)
    private Produit produit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bon_cmde_four_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "prod_bon_cmd_four", allowSetters = true)
    private BonDeCmdeFour bonDeCmdeFour;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "prod_bon_cmd_four", allowSetters = true)
    private Magasin magasin;
}