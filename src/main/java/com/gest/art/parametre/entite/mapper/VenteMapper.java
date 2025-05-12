package com.gest.art.parametre.entite.mapper;

import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.Entre;
import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.Vente;
import com.gest.art.parametre.entite.dto.ClientDTO;
import com.gest.art.parametre.entite.dto.EntreDTO;
import com.gest.art.parametre.entite.dto.EntreProduitDTO;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.entite.dto.MagasinDTO;
import com.gest.art.parametre.entite.dto.VenteDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LigneDeVenteMapper.class, MagasinMapper.class,ClientMapper.class})
public interface VenteMapper {

	VenteDTO toDTO(Vente vente);

	Vente toEntity(VenteDTO dto);


	LigneDeVenteDTO toLigneDeVenteDTO(LigneDeVente ligneDeVente);

	MagasinDTO toMagasinDTO(Magasin magasin);

	ClientDTO toClientDTO(Client client);




}
