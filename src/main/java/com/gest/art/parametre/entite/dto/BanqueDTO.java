package com.gest.art.parametre.entite.dto;
import com.gest.art.parametre.entite.Banque;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class BanqueDTO extends AbstractAuditingEntity {
	private static final long serialVersionUID = 1L;

	private String id;

	@NotBlank(message = "le code banque est obligatoire")
	private String codeBanque;

	@NotBlank(message = "le nom de la banque est obligatoire")
	private String libellebanque;

	private String contact;

	// Liste des IDs seulement pour éviter la surcharge
	private List<String> succursalesIds;
	private List<String> magasinsIds;

	public static BanqueDTO fromEntity(Banque banque) {
		if (banque == null) return null;

		return BanqueDTO.builder()
				.id(banque.getId())
				.codeBanque(banque.getCodeBanque())
				.libellebanque(banque.getLibellebanque())
				.contact(banque.getContact())
				.build();
	}

	public static Banque toEntity(BanqueDTO dto) {
		if (dto == null) return null;

		return Banque.builder()
				.id(dto.getId())
				.codeBanque(dto.getCodeBanque())
				.libellebanque(dto.getLibellebanque())
				.contact(dto.getContact())
				.build();
	}
}