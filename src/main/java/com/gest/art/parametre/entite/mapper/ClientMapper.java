package com.gest.art.parametre.entite.mapper;


import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.dto.ClientDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
	ClientDTO toDTO(Client client);
	Client toEntity(ClientDTO dto);
}
