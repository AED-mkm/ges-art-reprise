package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.TypeClient;
import com.gest.art.parametre.entite.dto.FournisseurDTO;
import com.gest.art.parametre.entite.dto.TypeClientDTO;
import com.gest.art.parametre.repository.TypeClientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class TypeClientService {
    private final Logger log = LoggerFactory.getLogger(TypeClientService.class);
    private final TypeClientRepository typeClientRepository;

    public TypeClientService(TypeClientRepository typeClientRepository) {
        this.typeClientRepository = typeClientRepository;
    }


    /**
     * Save Fournisseur dto.
     *
     * @param typeClientDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public TypeClientDTO save(final TypeClientDTO typeClientDTO) {
        log.debug("Request to save Fournisseur : {}", typeClientDTO);
        return TypeClientDTO.fromEntity(
                typeClientRepository.save(
                        TypeClientDTO.toEntity(typeClientDTO)));
    }

    /**
     * Update Fournisseur dto.
     *
     * @param typeClientDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public TypeClientDTO update(final TypeClientDTO typeClientDTO) {
        log.debug("Request to update Fournisseur : {}", typeClientDTO);
        return TypeClientDTO.fromEntity(
                typeClientRepository.save(
                        TypeClientDTO.toEntity(typeClientDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<TypeClientDTO> findAll() {
        log.debug("Request to get all Fournisseurs");
        return typeClientRepository
                .findAll()
                .stream()
                .map(TypeClientDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public TypeClientDTO findOne(String id) {
        log.debug("Request to get TypeClientDTO : {}", id);
        return typeClientRepository.findById(id)
                .map(TypeClientDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("non trouver"));
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(final String id) {
        log.debug("Request to delete Fournisseur : {}", id);
        if (id == null) {
            log.info("L'id est null");
            return;
        }
        typeClientRepository.deleteById(id);
    }

    public Page<TypeClient> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return typeClientRepository.findAll(pageable);
    }
}
