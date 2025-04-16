package com.gest.art.parametre.entite.dto;


import com.gest.art.parametre.entite.Password;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO extends AbstractAuditingEntity {
    private String id;
    private String libelle;
    private boolean etat;
    private boolean deleted;

    public static PasswordDTO fromEntity(Password password) {
        if (password == null) {
            return null;
        }
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setId(password.getId());
        passwordDTO.setLibelle(password.getLibelle());
        passwordDTO.setEtat(password.isEtat());
        passwordDTO.setDeleted(password.isDeleted());
        return passwordDTO;
    }

    public static Password toEntity(PasswordDTO passwordDTO) {
        return Password.builder()
                .id(passwordDTO.getId())
                .libelle(passwordDTO.getLibelle())
                .etat(passwordDTO.isEtat())
                // .deleted(passwordDTO.isDeleted())
                .build();
    }
}
