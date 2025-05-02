package com.gest.art.security.user.dto;

import com.gest.art.security.auditing.AbstractAuditingEntity;
import com.gest.art.security.user.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateProfilDTO extends AbstractAuditingEntity{
    private String id;
    private String email;
    private String genre;
    private String telephone;
    private Instant dateNaiss;
    private String nom;

    private String prenom;

    private String username;
    private String imageProfile;

    public static UserUpdateProfilDTO fromEntity(User utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        return UserUpdateProfilDTO.builder()
                .id(utilisateur.getId())
                .username(utilisateur.getUsername())
                .genre(utilisateur.getGenre())
                .email(utilisateur.getEmail())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .telephone(utilisateur.getTelephone())
                .dateNaiss(utilisateur.getDateNaiss())
                .imageProfile((utilisateur.getImageProfile()))
                .build();


    }

    public static User toEntity(UserUpdateProfilDTO utilisateurDto) {
        if (utilisateurDto == null) {
            return null;
        }
        User utilisateur = new User();
        utilisateur.setId(utilisateurDto.getId());
        utilisateur.setUsername(utilisateurDto.getUsername());
        utilisateur.setGenre(utilisateurDto.getGenre());
        utilisateur.setEmail(utilisateurDto.getEmail());
        utilisateur.setNom(utilisateurDto.getNom());
        utilisateur.setPrenom(utilisateurDto.getPrenom());
        utilisateur.setTelephone(utilisateurDto.getTelephone());
        utilisateur.setDateNaiss(utilisateurDto.getDateNaiss());
        utilisateur.setImageProfile(utilisateurDto.getImageProfile());
        return utilisateur;
    }


}
