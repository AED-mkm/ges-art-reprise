package com.gest.art.parametre.entite.historique;

import com.gest.art.parametre.entite.Produit;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @author Moctar
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_historique_cout")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_historique_cout set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class HistoriqueCout extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_hist_cout", sequenceName = "seq_hist_cout",
            initialValue = 8010, allocationSize = 5)
    private Long id;
    @Column(name = "ncout_achat")
    private BigDecimal nouveauCoutAchat = BigDecimal.ZERO;
    @Column(name = "acout_achat")
    private BigDecimal ancienCoutAchat = BigDecimal.ZERO;
    @ManyToOne
    private Produit produit;
}