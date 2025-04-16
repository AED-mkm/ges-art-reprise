package com.gest.art.security.config.mailer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import com.gest.art.security.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "emails")
@Cacheable(value = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
@SQLDelete(sql = "UPDATE emails SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class EmailDetails extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    private String toEmail;
    private String body;
    private String subject;
    private String attachment;
    @ManyToOne
    @JsonIgnoreProperties(value = {"users"}, allowSetters = true)
    private User user;
}
