package com.gest.art.parametre.entite.dto;



import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.parametre.entite.ProduitBordLiv;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BordereauLivraisonDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String numBordereau;
	private LocalDate dateBordereau;
	private BigDecimal netApayer = BigDecimal.ZERO ;
	private BigDecimal totalTransport = BigDecimal.ZERO;
	private BigDecimal totalEmballage = BigDecimal.ZERO;
	private BigDecimal montantPayer = BigDecimal.ZERO;
	private BigDecimal tauxTva = BigDecimal.ZERO;
	private String etatBordereau;
	private BigDecimal montantTva = BigDecimal.ZERO;
	private BigDecimal tauxBic = BigDecimal.ZERO;
	private BigDecimal montantBic = BigDecimal.ZERO;
	private BigDecimal montantTtc = BigDecimal.ZERO;

	// Références aux IDs des entités liées
	private String clientId;
	private String magasinId;
	private List<ProduitBordLivDTO> produitBordLivs;
	private List<String> taxesCochees;


	public static BordereauLivraisonDTO fromEntity(BordereauLivraison bordereau) {
		if (bordereau == null) {
			return null;
		}
	BordereauLivraisonDTO  bordereauDTO = new BordereauLivraisonDTO();
	bordereauDTO.setId(bordereau.getId());
	bordereauDTO.setNumBordereau(bordereau.getNumBordereau());
	bordereauDTO.setDateBordereau(bordereau.getDateBordereau());
	bordereauDTO.setNetApayer(bordereau.getNetApayer());
	bordereauDTO.setTotalTransport(bordereau.getTotalTransport());
	bordereauDTO.setTotalEmballage(bordereau.getTotalEmballage());
	bordereauDTO.setMontantPayer(bordereau.getMontantPayer());
	bordereauDTO.setTauxBic(bordereau.getTauxBic());
	bordereauDTO.setMontantTva(bordereau.getMontantTva());
	bordereauDTO.setEtatBordereau(bordereau.getEtatBordereau());
	bordereauDTO.setTauxTva(bordereau.getTauxTva());
	bordereauDTO.setMontantBic(bordereau.getMontantBic());
	bordereauDTO.setClientId(bordereau.getClient().getId());
	bordereauDTO.setMagasinId(bordereau.getMagasin().getId());
	return bordereauDTO;
	}

	public static BordereauLivraison toEntity(BordereauLivraisonDTO dto) {
		return BordereauLivraison.builder()
				.id(dto.getId())
				.numBordereau(dto.getNumBordereau())
				.dateBordereau(dto.getDateBordereau())
				.netApayer(dto.getNetApayer())
				.totalTransport(dto.getTotalTransport())
				.totalEmballage(dto.getTotalEmballage() !=null ? dto.getTotalEmballage(): BigDecimal.ZERO)
				.montantPayer(dto.getMontantPayer() !=null ? dto.getMontantPayer(): BigDecimal.ZERO)
				.tauxTva(dto.getTauxTva() !=null ? dto.getTauxTva(): BigDecimal.ZERO)
				.etatBordereau(dto.getEtatBordereau())
				.montantTva(dto.getMontantTva() !=null ? dto.getMontantTva(): BigDecimal.ZERO)
				.tauxBic(dto.getTauxBic() !=null ? dto.getTauxBic(): BigDecimal.ZERO)
				.montantBic(dto.getMontantBic() !=null ? dto.getMontantBic(): BigDecimal.ZERO)
				.montantTtc(dto.getMontantTtc() !=null ? dto.getMontantTtc(): BigDecimal.ZERO)
				.build();

	}
}