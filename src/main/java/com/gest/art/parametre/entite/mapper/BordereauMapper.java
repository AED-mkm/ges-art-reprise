package com.gest.art.parametre.entite.mapper;

import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.parametre.entite.ProduitBordLiv;
import com.gest.art.parametre.entite.dto.BordereauLivraisonDTO;
import com.gest.art.parametre.entite.dto.ProduitBordLivDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MagasinMapper.class, ClientMapper.class,ProduitBordLivMapper.class})
public interface BordereauMapper {

	BordereauLivraisonDTO toDTO(BordereauLivraison bordereauLivraison);

	BordereauLivraison toEntity(BordereauLivraisonDTO dto);

	ProduitBordLivDTO toProduitBordLivDTO(ProduitBordLiv produitBordLiv);

	ProduitBordLiv toProduitBordLiv(ProduitBordLivDTO dto);

}
