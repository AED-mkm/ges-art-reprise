package com.gest.art.parametre.entite.mapper;

import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LigneDeVenteMapper {

	LigneDeVente toEntity(LigneDeVenteDTO dto);

	LigneDeVenteDTO toDTO(LigneDeVente entity);

	List<LigneDeVente> toEntityList(List<LigneDeVenteDTO> dtos);

	List<LigneDeVenteDTO> toDTOList(List<LigneDeVente> entities);
}
