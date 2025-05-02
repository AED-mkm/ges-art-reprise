package com.gest.art.parametre.entite;


import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_transfert")
@Cacheable(value = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
@SQLDelete(sql = "UPDATE kg_transfert SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Transfert extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@ManyToOne
	@JoinColumn(name = "magasin_source_id")
	private Magasin magasinSource;

	@ManyToOne
	@JoinColumn(name = "magasin_destination_id")
	private Magasin magasinDestination;

	private LocalDateTime dateTransfert;
	private String motifTransfert;

	@OneToMany(mappedBy = "transfert", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransfertProduit> transfertProduits;

}
