package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.dto.MagasinDTO;
import com.gest.art.parametre.repository.MagasinRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class MagasinService {
    private final Logger log = LoggerFactory.getLogger(MagasinService.class);
    private final MagasinRepository magasinRepository;

    public MagasinService(MagasinRepository magasinRepository) {
        this.magasinRepository = magasinRepository;

    }





    /**
     * Save Magasin dto.
     *
     * @param MagasinDTO the Magasin dto
     * @return the Magasin dto
     */
    public MagasinDTO save(final MagasinDTO MagasinDTO) {
        log.debug("Request to save Magasin : {}", MagasinDTO);
        return MagasinDTO.fromEntity(
                magasinRepository.save(
                        MagasinDTO.toEntity(MagasinDTO)));
    }

    /**
     * Update Magasin dto.
     *
     * @param MagasinDTO the Magasin dto
     * @return the Magasin dto
     */
    public MagasinDTO update(final MagasinDTO MagasinDTO) {
        log.debug("Request to update Magasin : {}", MagasinDTO);
        return MagasinDTO.fromEntity(
                magasinRepository.save(
                        MagasinDTO.toEntity(MagasinDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<MagasinDTO> findAll() {
        log.debug("Request to get all Magasins");
        return magasinRepository
                .findAll()
                .stream()
                .map(MagasinDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public MagasinDTO findOne(String id) {
        log.debug("Request to get MagasinDTO : {}", id);
        return magasinRepository.findById(id)
                .map(MagasinDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("non trouver"));
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(final String id) {
        log.debug("Request to delete Magasin : {}", id);
        if (id == null) {
            log.info("L'id est null");
            return;
        }
        magasinRepository.deleteById(id);
    }

}
