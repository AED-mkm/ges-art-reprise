package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.enums.EtatBordereau;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsBordereauDTO {

	private String id;
	private String clientId;
	private  String denomination;

	private EtatBordereau etatBordereau;
	private String numBordereau;
	private LocalDate dateBordereau;
	private BigDecimal montantPayer;
	private BigDecimal netApayer;
	private BigDecimal montantRestant;

	public DetailsBordereauDTO(BordereauLivraison bordereauLivraison){
		this.id = bordereauLivraison.getId();
		this.etatBordereau = bordereauLivraison.getEtatBordereau();
		this.numBordereau = bordereauLivraison.getNumBordereau();
		this.dateBordereau = bordereauLivraison.getDateBordereau();
		this.montantPayer=bordereauLivraison.getMontantPayer();
		this.netApayer = bordereauLivraison.getNetApayer();
		this.montantRestant = bordereauLivraison.getMontantRestant();

		Client client = bordereauLivraison.getClient();
		if (client != null) {
			this.clientId = client.getId();
			this.denomination = client.getDenomination();

		}
	}

}
