package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.dto.ProduitDTO;
import com.gest.art.parametre.repository.ProduitRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ProduitService {
    private final Logger log = LoggerFactory.getLogger(ProduitService.class);
    private final ProduitRepository produitRepository;


    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;

    }

    /**
     * Save Produit dto.
     *
     * @param produitDTO the Produit dto
     * @return the Produit dto
     */
    public ProduitDTO save(final ProduitDTO produitDTO) {
        log.debug("Request to save Produit : {}", produitDTO);
        return ProduitDTO.fromEntity(
                produitRepository.save(
                        ProduitDTO.toEntity(produitDTO)));
    }

    /**
     * Update Produit dto.
     *
     * @param produitDTO the Produit dto
     * @return the Produit dto
     */
    public ProduitDTO update(final ProduitDTO produitDTO) {
        log.debug("Request to update Produit : {}", produitDTO);
        return ProduitDTO.fromEntity(
                produitRepository.save(
                        ProduitDTO.toEntity(produitDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ProduitDTO> findAll() {
        log.debug("Request to get all Produits");
        return produitRepository
                .findAll()
                .stream()
                .map(ProduitDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ProduitDTO findOne(String id) {
        log.debug("Request to get ProduitDTO : {}", id);
        return produitRepository.findById(id)
                .map(ProduitDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("non trouver"));
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(final String id) {
        log.debug("Request to delete Produit : {}", id);
        if (id == null) {
            log.info("L'id est null");
            return;
        }
        produitRepository.deleteById(String.valueOf(id));
    }
}
