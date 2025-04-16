package com.gest.art.security.user;

import com.gest.art.parametre.entite.Magasin;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import com.gest.art.security.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_users")
@SQLDelete(sql = "UPDATE kg_users SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class User extends AbstractAuditingEntity implements UserDetails, Serializable {

    @Column(unique = true, length = 100, nullable = true)
    public String username;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String prenom;
    private String genre;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, length = 100, nullable = false)
    private String telephone;
    private String police;
    private String password;
    private Boolean enabled;
    private String verificationToken;
    @Column(name = "date_debut", nullable = true)
    private Instant dateDebut;
    @Column(name = "date_fin", nullable = true)
    private Instant dateFin;
    @Column(name = "date_naiss", nullable = true)
    private Instant dateNaiss;
    @Enumerated(EnumType.STRING)
    private Statut statut;
    private String imageProfile;
    @ManyToOne
    @JoinColumn(name = "magasin_id")
    private Magasin magasin;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "kg_users_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }
}
