package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.TypeReglement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReglementGroupResponseDTO {
	private String id;
	private String numBordereau;
	private LocalDate dateRegl;
	//private BigDecimal montantTtc;
	private BigDecimal montantRegl;
	private String typeReglement;
	private String client;
	List<BordereauReglementDTO> bordereauReglement;
	private BigDecimal montantRestantTotal;

}
