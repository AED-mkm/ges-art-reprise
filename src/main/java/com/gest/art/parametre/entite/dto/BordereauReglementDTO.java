package com.gest.art.parametre.entite.dto;

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
public class BordereauReglementDTO {
	private String bordereauId;
	private LocalDate dateRegl;
	private String numBordereau;
	//private BigDecimal netApayer;
	private BigDecimal montantPayer;
	private BigDecimal montantRestant;
}
