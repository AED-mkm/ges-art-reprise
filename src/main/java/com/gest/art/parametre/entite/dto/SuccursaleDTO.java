package com.gest.art.parametre.entite.dto;



import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.Operation;
import com.gest.art.parametre.entite.Succursale;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuccursaleDTO extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotBlank(message = "le code de la succ est obligatoire")
	private String codeSucc;
	@NotBlank(message = "le libelle de la succ est obligatoire")
	private String libelleSucc;
	@NotBlank(message = "le contact de la succ est obligatoire")
	private String contactSucc;
	private BanqueDTO banqueDTO;

	public static SuccursaleDTO fromEntity(Succursale succursale) {
		if (succursale == null) {
			return null;
		}
	    SuccursaleDTO succDTO = new SuccursaleDTO();
		succDTO.setId(succursale.getId());
		succDTO.setCodeSucc(succursale.getCodeSucc());
		succDTO.setLibelleSucc(succursale.getLibelleSucc());
		succDTO.setContactSucc(succursale.getContactSucc());
		succDTO.setBanqueDTO(BanqueDTO.fromEntity(succursale.getBanque()));
		return succDTO;
	}

	public static Succursale toEntity(SuccursaleDTO dto) {
		return Succursale.builder()
				.id(dto.getId())
				.codeSucc(dto.getCodeSucc())
				.libelleSucc(dto.getLibelleSucc())
				.contactSucc(dto.getContactSucc())
				.banque(BanqueDTO.toEntity(dto.getBanqueDTO()))
				.build();
	}

}