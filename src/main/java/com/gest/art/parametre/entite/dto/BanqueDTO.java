package com.gest.art.parametre.entite.dto;
import com.gest.art.parametre.entite.Banque;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BanqueDTO extends AbstractAuditingEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	@NotBlank(message = "le code banque est obligatoire")
	private String codeBanque;
	@NotBlank(message = "le nom de la banque est obligatoire")
	private String libellebanque;
	@NotBlank(message = "le contact de la banque est obligatoire")
	private String contact;

	// Liste des IDs seulement pour éviter la surcharge
	/*private List<String> succursalesIds;
	private List<String> magasinsIds;*/

	public static BanqueDTO fromEntity(Banque banque) {
		if (banque == null) {
			return null;
		}
		BanqueDTO banqueDTO = new BanqueDTO();
		banqueDTO.setId(banque.getId());
		banqueDTO.setCodeBanque(banque.getCodeBanque());
		banqueDTO.setLibellebanque(banque.getLibellebanque());
		banqueDTO.setContact(banque.getContact());
		return banqueDTO;
	}

	public static Banque toEntity(BanqueDTO banqueDTO) {
		return Banque.builder()
				.id(banqueDTO.getId())
				.codeBanque(banqueDTO.getCodeBanque())
				.libellebanque(banqueDTO.getLibellebanque())
				.contact(banqueDTO.getContact())
				.build();
	}

}