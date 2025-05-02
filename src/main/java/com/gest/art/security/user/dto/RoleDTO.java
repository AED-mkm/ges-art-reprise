package com.gest.art.security.user.dto;

import com.gest.art.security.auditing.AbstractAuditingEntity;
import com.gest.art.security.user.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends AbstractAuditingEntity {

    private String id;
    private String name;
    private String description;

    public static RoleDTO fromEntity(Role role) {
        if (role == null) {
            return null;
        }
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        return roleDTO;
    }

    public static Role toEntity(RoleDTO roleDTO) {
        return Role.builder()
                .id(roleDTO.getId())
                .name(roleDTO.getName())
                .description(roleDTO.getDescription())
                .build();
    }
}
