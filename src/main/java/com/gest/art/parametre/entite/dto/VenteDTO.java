package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Vente;
import com.gest.art.parametre.entite.enums.TypeVente;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VenteDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private LocalDate dateVente;
	private TypeVente typeVente;
	private String objet;
	private BigDecimal montantHt;
	private BigDecimal montantTva;
	private BigDecimal montantBic;
	private BigDecimal montantTTC;
	private Long factureId;

	// Références aux IDs des entités liées
	private String magasinId;
	private String clientId;
	private String taxeId;
	private List<String> lignesDeVenteIds;

	public static VenteDTO fromEntity(Vente vente) {
		if (vente == null) {
			return null;
		}

		return VenteDTO.builder()
				.id(vente.getId())
				.dateVente(vente.getDateVente())
				.typeVente(vente.getTypeVente())
				.objet(vente.getObjet())
				.montantHt(vente.getMontantHt())
				.montantTva(vente.getMontantTva())
				.montantBic(vente.getMontantBic())
				.montantTTC(vente.getMontantTTC())
				.factureId(vente.getFactureId())
				.magasinId(vente.getMagasin() != null ? vente.getMagasin().getId(): null)
				.clientId(vente.getClient() != null ? vente.getClient().getId() : null)
				.taxeId(vente.getTaxe() != null ? vente.getTaxe().getId() : null)
				.lignesDeVenteIds(vente.getLignesDeVente() != null ?
						vente.getLignesDeVente().stream()
								.map(ligne -> ligne.getId())
								.collect(Collectors.toList()) : null)
				/*.createdBy(vente.getCreatedBy())
				.lastModifiedBy(vente.getLastModifiedBy())
				.createdDate(vente.getCreatedDate())
				.lastModifiedDate(vente.getLastModifiedDate())*/
				.build();
	}

	public static Vente toEntity(VenteDTO dto) {
		if (dto == null) {
			return null;
		}

		return Vente.builder()
				.id(dto.getId())
				.dateVente(dto.getDateVente())
				.typeVente(dto.getTypeVente())
				.objet(dto.getObjet())
				.montantHt(dto.getMontantHt() != null ? dto.getMontantHt() : BigDecimal.ZERO)
				.montantTva(dto.getMontantTva() != null ? dto.getMontantTva() : BigDecimal.ZERO)
				.montantBic(dto.getMontantBic() != null ? dto.getMontantBic() : BigDecimal.ZERO)
				.montantTTC(dto.getMontantTTC() != null ? dto.getMontantTTC() : BigDecimal.ZERO)
				.factureId(dto.getFactureId())
				// Les relations doivent être gérées séparément
				.build();
	}
}