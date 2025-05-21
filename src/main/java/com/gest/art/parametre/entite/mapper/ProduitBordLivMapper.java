package com.gest.art.parametre.entite.mapper;

import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.ProduitBordLiv;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.entite.dto.ProduitBordLivDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProduitBordLivMapper {

	ProduitBordLiv toEntity(ProduitBordLivDTO dto);

	ProduitBordLivDTO toDTO(ProduitBordLiv entity);

	List<ProduitBordLiv> toEntityList(List<ProduitBordLivDTO> dtos);

	List<ProduitBordLivDTO> toDTOList(List<ProduitBordLiv> entities);
}
