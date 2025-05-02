package com.gest.art.parametre.entite.mapper;

import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.dto.ProduitDTO;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface ProduitMapper {

	ProduitDTO toDTO(Produit produit);
	Produit toEntity(ProduitDTO dto);
}
