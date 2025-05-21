package com.gest.art.security.Utils.validator;

import com.gest.art.parametre.entite.dto.BordereauLivraisonDTO;
import com.gest.art.parametre.entite.dto.VenteDTO;
import jakarta.validation.ValidationException;

public class BorderearValidator {
	public static void validate(BordereauLivraisonDTO dto) {
		if(dto.getProduitBordLivs() ==null || dto.getProduitBordLivs().isEmpty())
		{
			throw new ValidationException("La liste des produits du bordereau ne doit pas etre vide!");
		}
		if(dto.getClientId()==null)
		{
			throw new ValidationException("Le client est obligatoire pour etablir un bordereau de livraison!");
		}

	}
}
