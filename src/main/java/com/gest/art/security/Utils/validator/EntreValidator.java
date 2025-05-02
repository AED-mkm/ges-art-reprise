package com.gest.art.security.Utils.validator;

import com.gest.art.parametre.entite.dto.EntreDTO;
import jakarta.validation.ValidationException;

import java.math.BigDecimal;

public class EntreValidator {
	public static void validate(EntreDTO dto) {
		if (dto.getObjet() == null || dto.getObjet().isBlank()) {
			throw new ValidationException("L'objet est obligatoire");
		}
		if (dto.getNumBordLiv() == null || dto.getNumBordLiv().isBlank()) {
			throw new ValidationException("Le numéro de bordereau est obligatoire");
		}
		// Validation des produits
		dto.getEntreProduits().forEach(ep -> {
			if (ep.getQuantite().compareTo(BigDecimal.ZERO) <= 0) {
				throw new ValidationException("La quantité doit être positive");
			}
			if (ep.getPrixEntre().compareTo(BigDecimal.ZERO) <= 0) {
				throw new ValidationException("Le prix doit être positif");
			}
		});
	}
}
