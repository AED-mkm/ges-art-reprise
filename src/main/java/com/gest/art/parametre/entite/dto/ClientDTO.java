package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Client;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO extends AbstractAuditingEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	private String codeClient;
	@NotBlank(message = "le nom du client est obligatoire")
	private String denomination;
	private String contactClient;
	private String adresseClient;

	// Références aux IDs des entités liées
	private TypeClientDTO typeClientDTO;
	/*private String magasinId;
	private List<String> ventesIds;
	private List<String> facturesIds;*/

	public static ClientDTO fromEntity(Client client) {
		if (client == null) {
			return null;
		}

		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setId( client.getId() );
		clientDTO.setCodeClient(client.getCodeClient());
		clientDTO.setDenomination(client.getDenomination());
		clientDTO.setContactClient(client.getContactClient());
		clientDTO.setAdresseClient(client.getAdresseClient());
		clientDTO.setTypeClientDTO(TypeClientDTO.fromEntity(client.getTypeClient()));
		return clientDTO ;
	}
	public static Client toEntity(ClientDTO dto) {
		if (dto == null) {
			return null;
		}
		return Client.builder()
				.id(dto.getId())
				.codeClient(dto.getCodeClient())
				.denomination(dto.getDenomination())
				.contactClient(dto.getContactClient())
				.adresseClient(dto.getAdresseClient())
				.typeClient(TypeClientDTO.toEntity(dto.getTypeClientDTO()))
				// Les relations doivent être gérées séparément
				.build();
	}
}