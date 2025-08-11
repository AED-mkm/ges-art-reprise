package com.gest.art.parametre.entite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReglementGroupRequestDTO {
	private String clientId;
	private List<String> bordereauIds;
	private BigDecimal montantTtc;
	private TypeReglementDTO typeReglementDTO;
	private String libelleRegl;

}
