package com.gest.art.security.token;

import com.gest.art.security.auditing.AbstractAuditingEntity;
import com.gest.art.security.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Token extends AbstractAuditingEntity {

    @Column(unique = true)
    public String token;
    public boolean revoked;
    public boolean expired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;
}
