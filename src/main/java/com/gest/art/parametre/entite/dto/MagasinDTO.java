package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.Magasin;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MagasinDTO extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String codeMagasin;
    @NotBlank(message = "le nom du magasin est obligatoire")
    private String nomMagasin;
    private String adresseMagasin;
    private String contactMagasin;
    private String responsableMag;

    // Références aux IDs des entités liées
  /*  private List<String> produitsIds;
    private List<String> fournisseursIds;
    private List<String> ventesIds;
    private List<String> usersIds;*/

    public static MagasinDTO fromEntity(Magasin magasin) {
        if (magasin == null) {
            return null;
        }
        MagasinDTO magasinDTO = new MagasinDTO();
        magasinDTO.setId(magasin.getId());
        magasinDTO.setCodeMagasin(magasin.getCodeMagasin());
        magasinDTO.setNomMagasin(magasin.getNomMagasin());
        magasinDTO.setAdresseMagasin(magasin.getAdresseMagasin());
        magasinDTO.setContactMagasin(magasin.getContactMagasin());
        magasinDTO.setResponsableMag(magasin.getResponsableMag());
        return magasinDTO;
    }

    public static Magasin toEntity(MagasinDTO dto) {
        return Magasin.builder()
                .id(dto.getId())
                .codeMagasin(dto.getCodeMagasin())
                .nomMagasin(dto.getNomMagasin())
                .adresseMagasin(dto.getAdresseMagasin())
                .contactMagasin(dto.getContactMagasin())
                .responsableMag(dto.getResponsableMag())
                // Les relations doivent être gérées séparément
                .build();
    }
}