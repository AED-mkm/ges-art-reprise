package com.gest.art.security.Utils.validator;

import com.gest.art.parametre.entite.dto.EntreDTO;
import com.gest.art.parametre.entite.dto.VenteDTO;
import jakarta.validation.ValidationException;

import java.math.BigDecimal;

public class VenteValidator {
	public static void validate(VenteDTO dto) {
		if(dto.getLignesDeVente() ==null || dto.getLignesDeVente().isEmpty())
		{
			throw new ValidationException("Lq liste des lignes de vente ne doit pas etre vide!");
		}
		if(dto.getClientDTO().getId()==null)
		{
			throw new ValidationException("Le client est obligatoire pour effectuer une vente!");
		}

	}
}
