package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.Facture;
import com.gest.art.parametre.entite.enums.TypeVente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultiCritereDTO
{
	private String ventId;
	private LocalDate dateVente;
	private TypeVente typeVente;
	private String numFacture;
	private String factureId;
	private String produitId;
	private String magasinId;
	private Integer codeprod;
	private String nomMagasin;
	private String libelle;
	private String clientId;
	private String denomination;
	private Instant dateDebut;
	private Instant dateFin;
	private Integer limit;

}
