package com.gest.art.parametre.entite.mapper;

import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.dto.MagasinDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface MagasinMapper {
	MagasinDTO toDTO(Magasin magasin);
	Magasin toEntity(MagasinDTO dto);
}
