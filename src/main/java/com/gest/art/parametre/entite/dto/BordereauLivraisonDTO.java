package com.gest.art.parametre.entite.dto;



import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BordereauLivraisonDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String numBordereau;
	private LocalDate dateBordereau;
	private double totalBordereau;
	private double totalGeneral;
	private double totalRemise;
	private double netApayer;
	private double totalTransport;
	private double totalEmballage;
	private double montantPayer;
	private double tauxTva;
	private String etatBordereau;
	private double montantTva;
	private int tauxBic;
	private double montantBic;
	private double montantTtc;

	// Références aux IDs des entités liées
	private String clientId;
	private String magasinId;

	public static BordereauLivraisonDTO fromEntity(BordereauLivraison bordereau) {
		if (bordereau == null) {
			return null;
		}

		return BordereauLivraisonDTO.builder()
				.id(bordereau.getId())
				.numBordereau(bordereau.getNumBordereau())
				.dateBordereau(bordereau.getDateBordereau())
				.totalBordereau(bordereau.getTotalBordereau())
				.totalGeneral(bordereau.getTotalGeneral())
				.totalRemise(bordereau.getTotalRemise())
				.netApayer(bordereau.getNetApayer())
				.totalTransport(bordereau.getTotalTransport())
				.totalEmballage(bordereau.getTotalEmballage())
				.montantPayer(bordereau.getMontantPayer())
				.tauxTva(bordereau.getTauxTva())
				.etatBordereau(bordereau.getEtatBordereau())
				.montantTva(bordereau.getMontantTva())
				.tauxBic(bordereau.getTauxBic())
				.montantBic(bordereau.getMontantBic())
				.montantTtc(bordereau.getMontantTtc())
				.clientId(bordereau.getClient() != null ? bordereau.getClient().getId() : null)
				.magasinId(bordereau.getMagasin() != null ? bordereau.getMagasin().getId() : null)
				/*.createdBy(bordereau.getCreatedBy())
				.lastModifiedBy(bordereau.getLastModifiedBy())
				.createdDate(bordereau.getCreatedDate())
				.lastModifiedDate(bordereau.getLastModifiedDate())*/
				.build();
	}

	public static BordereauLivraison toEntity(BordereauLivraisonDTO dto) {
		if (dto == null) {
			return null;
		}

		return BordereauLivraison.builder()
				.id(dto.getId())
				.numBordereau(dto.getNumBordereau())
				.dateBordereau(dto.getDateBordereau())
				.totalBordereau(dto.getTotalBordereau())
				.totalGeneral(dto.getTotalGeneral())
				.totalRemise(dto.getTotalRemise())
				.netApayer(dto.getNetApayer())
				.totalTransport(dto.getTotalTransport())
				.totalEmballage(dto.getTotalEmballage())
				.montantPayer(dto.getMontantPayer())
				.tauxTva(dto.getTauxTva())
				.etatBordereau(dto.getEtatBordereau())
				.montantTva(dto.getMontantTva())
				.tauxBic(dto.getTauxBic())
				.montantBic(dto.getMontantBic())
				.montantTtc(dto.getMontantTtc())
				// Les relations doivent être gérées séparément
				.build();
	}
}