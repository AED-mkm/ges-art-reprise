package com.gest.art.security.auditing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "created_by", nullable = true, length = 50, updatable = false)
    @JsonIgnore
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = true, updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @Column(name = "deleted_date", nullable = true)
    @JsonIgnore
    private Instant deletedDate;

    @Column(name = "deleted", nullable = true)
    @JsonIgnore
    private boolean deleted = Boolean.FALSE;

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = true, length = 50)
    @JsonIgnore
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = true)
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();

    @Column(name = "DeletedBy", length = 50)
    private String deletedBy;

}
