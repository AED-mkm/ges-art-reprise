package com.gest.art.parametre.entite.dto;

import com.gest.art.parametre.entite.Fournisseur;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @Size(max = 20, message = "Le code fournisseur ne doit pas dépasser 20 caractères")
    private String codeFour;

    @NotBlank(message = "Le nom du fournisseur est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nomFour;

    @Size(max = 200, message = "L'adresse ne doit pas dépasser 200 caractères")
    private String adresseFour;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Numéro de contact invalide")
    private String contactFour;

    private String magasinId;
    private List<String> bonDeCmdeFourIds;
    private List<String> entreIds;

    // Mapping from Entity to DTO
    public static FournisseurDTO fromEntity(Fournisseur fournisseur) {
        if (fournisseur == null) {
            return null;
        }

        return FournisseurDTO.builder()
                .id(fournisseur.getId())
                .codeFour(fournisseur.getCodeFour())
                .nomFour(fournisseur.getNomFour())
                .adresseFour(fournisseur.getAdresseFour())
                .contactFour(fournisseur.getContactFour())
                .magasinId(fournisseur.getMagasin() != null ?
                        fournisseur.getMagasin().getId() : null)
                .bonDeCmdeFourIds(fournisseur.getBonDeCmdeFours() != null ?
                        fournisseur.getBonDeCmdeFours().stream()
                                .map(bon -> bon.getId())
                                .toList() : null)
                .entreIds(fournisseur.getEntres() != null ?
                        fournisseur.getEntres().stream()
                                .map(entre -> entre.getId())
                                .toList() : null)
                .build();
    }

    // Mapping from DTO to Entity
    public static Fournisseur toEntity(FournisseurDTO dto) {
        if (dto == null) {
            return null;
        }

        return Fournisseur.builder()
                .id(dto.getId())
                .codeFour(dto.getCodeFour())
                .nomFour(dto.getNomFour())
                .adresseFour(dto.getAdresseFour())
                .contactFour(dto.getContactFour())
                // Relationships should be handled separately
                .build();
    }
}