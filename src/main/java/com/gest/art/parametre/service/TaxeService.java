package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.dto.BanqueDTO;
import com.gest.art.parametre.entite.dto.TaxeDTO;
import com.gest.art.parametre.repository.TaxeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class TaxeService {
    private final Logger log = LoggerFactory.getLogger(TaxeService.class);
    private final TaxeRepository taxeRepository;

    public TaxeService(TaxeRepository taxeRepository) {
        this.taxeRepository = taxeRepository;
    }


    /**
     * Save Fournisseur dto.
     *
     * @param taxeDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public TaxeDTO save(final TaxeDTO taxeDTO) {
        log.debug("Request to save Fournisseur : {}", taxeDTO);
        return TaxeDTO.fromEntity(
                taxeRepository.save(
                        TaxeDTO.toEntity(taxeDTO)));
    }

    /**
     * Update Fournisseur dto.
     *
     * @param taxeDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public TaxeDTO update(final TaxeDTO taxeDTO) {
        log.debug("Request to update Fournisseur : {}", taxeDTO);
        return TaxeDTO.fromEntity(
                taxeRepository.save(
                        TaxeDTO.toEntity(taxeDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<TaxeDTO> findAll() {
        log.debug("Request to get all Fournisseurs");
        return taxeRepository
                .findAll()
                .stream()
                .map(TaxeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public TaxeDTO findOne(String id) {
        log.debug("Request to get TypeClientDTO : {}", id);
        return taxeRepository.findById(id)
                .map(TaxeDTO::fromEntity)
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
        taxeRepository.deleteById(id);
    }
}
