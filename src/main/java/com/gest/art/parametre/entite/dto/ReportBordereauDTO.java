package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Facture;
import com.gest.art.parametre.entite.enums.TypeVente;
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
public class ReportBordereauDTO {
	private String ventId;
	private LocalDate dateVente;
	private TypeVente typeVente;
	private String objet;
	private String numFacture;
	private Facture facture;
	private BigDecimal montantHt;
	private BigDecimal montantTva;
	private BigDecimal montantBic;
	private BigDecimal montantTTC;
	private String factureId;
	private String produitId;
	private Integer codeprod;
	private String libelle;
	private BigDecimal qteVente;
	private BigDecimal prixUnitaire;
	private BigDecimal prixTotal;
	private String magasinId;
	private String clientId;
	private String taxeId;
	private String lignesDeVenteId;
}
