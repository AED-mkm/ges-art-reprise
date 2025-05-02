package com.gest.art.parametre.entite.mapper;

import com.gest.art.parametre.entite.Fournisseur;
import com.gest.art.parametre.entite.dto.FournisseurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FournisseurMapper {

	FournisseurDTO toDTO(Fournisseur fournisseur);
	Fournisseur toEntity(FournisseurDTO dto);
}
