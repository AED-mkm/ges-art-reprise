package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Facture;
import com.gest.art.parametre.entite.Vente;
import com.gest.art.parametre.entite.enums.TypeVente;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
	private String factureId;
	private Facture facture;
	private List<String> taxesCochees;
	// Références aux IDs des entités liées
	//private String magasinId;
	private MagasinDTO magasinDTO;
	//private String clientId;
	private ClientDTO clientDTO;
	//private String taxeId;
	private TaxeDTO taxeDTO;
	private List<LigneDeVenteDTO> lignesDeVente;

	public static VenteDTO fromEntity(Vente vente) {
		if (vente == null) {
			return null;
		}
	VenteDTO venteDTO = new VenteDTO();
	venteDTO.setId(vente.getId());
	venteDTO.setDateVente(vente.getDateVente());
	venteDTO.setTypeVente(vente.getTypeVente());
	venteDTO.setObjet(vente.getObjet());
	venteDTO.setMontantHt(vente.getMontantHt());
	venteDTO.setMontantTva(vente.getMontantTva());
	venteDTO.setMontantBic(vente.getMontantBic());
	venteDTO.setMontantTTC(vente.getMontantTTC());
	venteDTO.setFactureId(vente.getFactureId());
	venteDTO.setTaxeDTO(TaxeDTO.fromEntity(vente.getTaxe()));
	venteDTO.setMagasinDTO(venteDTO.getMagasinDTO());
	//venteDTO.setMagasinId(vente.getMagasin().getId());
	//venteDTO.setClientId(vente.getClient().getId());
	venteDTO.setClientDTO(venteDTO.getClientDTO());
	return venteDTO;
	}

	public static Vente toEntity(VenteDTO dto) {
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