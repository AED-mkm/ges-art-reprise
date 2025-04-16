package com.gest.art.security.user;

import com.gest.art.security.repository.RoleRepository;
import com.gest.art.security.user.dto.RoleDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    public Role save(Role role) {
        return repository.save(role);
    }

    public List<RoleDTO> findAll() {
        return repository.findAll().stream().map(RoleDTO::fromEntity).collect(Collectors.toList());

    }

    public RoleDTO findById(String id) {
        return repository.findById(id).map(RoleDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "aucun Role trouvé" + id
                ));
    }

    public RoleDTO findByName(String name) {
        return repository.findByName(name).stream().findFirst()
                .map(RoleDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "aucun Role trouvé" + name
                ));
    }

    public void delete(String id) {
        if (id == null) {
            log.error("Aucun role à supprimer");
        }
        assert id != null;
        repository.deleteById(id);
    }
}
