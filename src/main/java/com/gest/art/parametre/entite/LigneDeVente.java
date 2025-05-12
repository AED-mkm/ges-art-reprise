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
import jakarta.validation.constraints.Positive;
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
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_ligne_vente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_ligne_vente set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class LigneDeVente extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private String id;
	@Column(name = "qte_vente")
	@Positive
	private BigDecimal qteVente;
	@Column(name = "prix_unitaire")
	@Positive
	private BigDecimal prixUnitaire;
	@Column(name = "prix_total")
	private BigDecimal prixTotal;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vente", referencedColumnName = "id")
	@JsonIgnoreProperties(value = "vente", allowSetters = true)
	private Vente vente;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produit", referencedColumnName = "id")
	@JsonIgnoreProperties(value = "produit", allowSetters = true)
	private Produit produit;
}
