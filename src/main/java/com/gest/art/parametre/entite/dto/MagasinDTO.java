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
    private List<String> produitsIds;
    private List<String> fournisseursIds;
    private List<String> ventesIds;
    private List<String> usersIds;

    public static MagasinDTO fromEntity(Magasin magasin) {
        if (magasin == null) {
            return null;
        }

        return MagasinDTO.builder()
                .id(magasin.getId())
                .codeMagasin(magasin.getCodeMagasin())
                .nomMagasin(magasin.getNomMagasin())
                .adresseMagasin(magasin.getAdresseMagasin())
                .contactMagasin(magasin.getContactMagasin())
                .responsableMag(magasin.getResponsableMag())
                .produitsIds(magasin.getProduits() != null ?
                        magasin.getProduits().stream()
                                .map(produit -> produit.getId())
                                .collect(Collectors.toList()) : null)
                .fournisseursIds(magasin.getFournisseurs() != null ?
                        magasin.getFournisseurs().stream()
                                .map(fournisseur -> fournisseur.getId())
                                .collect(Collectors.toList()) : null)
                .ventesIds(magasin.getVentes() != null ?
                        magasin.getVentes().stream()
                                .map(vente -> vente.getId())
                                .collect(Collectors.toList()) : null)
                .usersIds(magasin.getUsers() != null ?
                        magasin.getUsers().stream()
                                .map(user -> user.getId())
                                .collect(Collectors.toList()) : null)
                /* .createdBy(magasin.getCreatedBy())
                .lastModifiedBy(magasin.getLastModifiedBy())
                .createdDate(magasin.getCreatedDate())
                .lastModifiedDate(magasin.getLastModifiedDate())*/
                .build();
    }

    public static Magasin toEntity(MagasinDTO dto) {
        if (dto == null) {
            return null;
        }

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