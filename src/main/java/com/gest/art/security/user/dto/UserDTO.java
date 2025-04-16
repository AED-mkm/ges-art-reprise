package com.gest.art.security.user.dto;

import com.gest.art.parametre.entite.dto.MagasinDTO;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import com.gest.art.security.user.Role;
import com.gest.art.security.user.Statut;
import com.gest.art.security.user.User;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends AbstractAuditingEntity {
    private String id;

    private String email;

    private String telephone;

    private String nom;

    private String prenom;

    private String username;

    private String genre;

    private String police;

    private String password;
    private Instant dateNaiss;
    private Instant dateDebut;

    private Instant dateFin;

    private Statut statut;

    private MagasinDTO magasinDTO;

    private Boolean enabled;

    private Set<Role> roleDTOS;

    public static UserDTO fromEntity(User utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(utilisateur.getId());
        userDTO.setStatut(utilisateur.getStatut());
        userDTO.setPolice(utilisateur.getPolice());
        userDTO.setUsername(utilisateur.getUsername());
        userDTO.setGenre(utilisateur.getGenre());
        userDTO.setEmail(utilisateur.getEmail());
        userDTO.setNom(utilisateur.getNom());
        userDTO.setPrenom(utilisateur.getPrenom());
        userDTO.setTelephone(utilisateur.getTelephone());
        userDTO.setDateDebut(utilisateur.getDateDebut());
        userDTO.setPassword(utilisateur.getPassword());
        userDTO.setDateNaiss(utilisateur.getDateNaiss());
        userDTO.setDateFin(utilisateur.getDateFin());

        userDTO.setEnabled(utilisateur.getEnabled());
        if (utilisateur.getMagasin() != null) {
            userDTO.setMagasinDTO(MagasinDTO.fromEntity(utilisateur.getMagasin()));
        } else {
            userDTO.setMagasinDTO(null);
        }
        if (utilisateur.getRoles() != null) {
            userDTO.setRoleDTOS(new HashSet<>(utilisateur.getRoles()));
        } else {
            userDTO.setRoleDTOS(null);
        }

        return userDTO;


    }

    public static User toEntity(UserDTO userDTO) {

        return User.builder()
                .id(userDTO.getId())
                .statut(userDTO.getStatut())
                .police(userDTO.getPolice())
                .username(userDTO.getUsername())
                .genre(userDTO.getGenre())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .nom(userDTO.getNom())
                .prenom(userDTO.getPrenom())
                .telephone(userDTO.getTelephone())
                .dateNaiss(userDTO.getDateNaiss())
                .dateDebut(userDTO.getDateDebut())
                .dateFin(userDTO.getDateFin())
                .magasin(userDTO.getMagasinDTO() != null ? MagasinDTO.toEntity(userDTO.getMagasinDTO()) : null)
                .enabled(userDTO.getEnabled())
                .roles(userDTO.getRoleDTOS() != null ? new HashSet<>(userDTO.getRoleDTOS()) : null)
                .build();
    }


}
