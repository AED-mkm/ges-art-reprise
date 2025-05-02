package com.gest.art.parametre.entite.mapper;

import com.gest.art.parametre.entite.Entre;
import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.parametre.entite.dto.EntreDTO;
import com.gest.art.parametre.entite.dto.EntreProduitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MagasinMapper.class, FournisseurMapper.class,ProduitMapper.class})
public interface EntreMapper {

	EntreDTO toDTO(Entre entre);

	Entre toEntity(EntreDTO dto);

	EntreProduitDTO toEntreProduitDTO(EntreProduit entreProduit);

	EntreProduit toEntreProduit(EntreProduitDTO dto);

}
