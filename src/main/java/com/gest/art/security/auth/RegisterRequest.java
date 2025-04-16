package com.gest.art.security.auth;

import com.gest.art.security.user.Role;
import com.gest.art.security.user.Statut;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String id;

    private String email;

    private String matricule;

    private String telephone;

    private String nom;

    private String prenom;

    private String username;

    private String police;

    private String password;

    private Instant dateDebut;

    private Instant dateFin;

    private Statut statut;

    private Boolean enabled;

    private Collection<Role> roleDTOS;
}
