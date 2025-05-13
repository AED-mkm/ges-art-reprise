package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.TypeReglement;
import com.gest.art.parametre.entite.dto.BanqueDTO;
import com.gest.art.parametre.entite.dto.TypeReglementDTO;
import com.gest.art.parametre.repository.TypeReglRepository;
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
public class TypeReglementService {
    private final Logger log = LoggerFactory.getLogger(TypeReglementService.class);
    private final TypeReglRepository typeReglRepository;

    public TypeReglementService(TypeReglRepository typeReglRepository) {
        this.typeReglRepository = typeReglRepository;
    }


    /**
     * Save Fournisseur dto.
     *
     * @param typeReglementDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public TypeReglementDTO save(final TypeReglementDTO typeReglementDTO) {
        log.debug("Request to save Fournisseur : {}", typeReglementDTO);
        return TypeReglementDTO.fromEntity(
                typeReglRepository.save(
                        TypeReglementDTO.toEntity(typeReglementDTO)));
    }

    /**
     * Update Fournisseur dto.
     *
     * @param typeReglementDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public TypeReglementDTO update(final TypeReglementDTO typeReglementDTO) {
        log.debug("Request to update Fournisseur : {}", typeReglementDTO);
        return TypeReglementDTO.fromEntity(
                typeReglRepository.save(
                        TypeReglementDTO.toEntity(typeReglementDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<TypeReglementDTO> findAll() {
        log.debug("Request to get all Fournisseurs");
        return typeReglRepository
                .findAll()
                .stream()
                .map(TypeReglementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public TypeReglementDTO findOne(String id) {
        log.debug("Request to get TypeClientDTO : {}", id);
        return typeReglRepository.findById(id)
                .map(TypeReglementDTO::fromEntity)
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
        typeReglRepository.deleteById(id);
    }

    public Page<TypeReglement> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return typeReglRepository.findAll(pageable);
    }
}
