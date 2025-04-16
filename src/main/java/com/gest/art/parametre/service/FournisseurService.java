package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.dto.FournisseurDTO;
import com.gest.art.parametre.repository.FournisseurRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class FournisseurService {
    private final Logger log = LoggerFactory.getLogger(FournisseurService.class);
    private final FournisseurRepository fournisseurRepository;

    public FournisseurService(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }


    /**
     * Save Fournisseur dto.
     *
     * @param fournisseurDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public FournisseurDTO save(final FournisseurDTO fournisseurDTO) {
        log.debug("Request to save Fournisseur : {}", fournisseurDTO);
        return FournisseurDTO.fromEntity(
                fournisseurRepository.save(
                        FournisseurDTO.toEntity(fournisseurDTO)));
    }

    /**
     * Update Fournisseur dto.
     *
     * @param fournisseurDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public FournisseurDTO update(final FournisseurDTO fournisseurDTO) {
        log.debug("Request to update Fournisseur : {}", fournisseurDTO);
        return FournisseurDTO.fromEntity(
                fournisseurRepository.save(
                        FournisseurDTO.toEntity(fournisseurDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<FournisseurDTO> findAll() {
        log.debug("Request to get all Fournisseurs");
        return fournisseurRepository
                .findAll()
                .stream()
                .map(FournisseurDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public FournisseurDTO findOne(String id) {
        log.debug("Request to get FournisseurDTO : {}", id);
        return fournisseurRepository.findById(id)
                .map(FournisseurDTO::fromEntity)
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
        fournisseurRepository.deleteById(id);
    }
}
