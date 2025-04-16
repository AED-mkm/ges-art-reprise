package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.dto.BanqueDTO;
import com.gest.art.parametre.entite.dto.ClientDTO;
import com.gest.art.parametre.repository.BanqueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class BanqueService {
    private final Logger log = LoggerFactory.getLogger(BanqueService.class);
    private final BanqueRepository banqueRepository;

    public BanqueService(BanqueRepository banqueRepository) {
        this.banqueRepository = banqueRepository;
    }


    /**
     * Save Fournisseur dto.
     *
     * @param banqueDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public BanqueDTO save(final BanqueDTO banqueDTO) {
        log.debug("Request to save Fournisseur : {}", banqueDTO);
        return BanqueDTO.fromEntity(
                banqueRepository.save(
                        BanqueDTO.toEntity(banqueDTO)));
    }

    /**
     * Update Fournisseur dto.
     *
     * @param banqueDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public BanqueDTO update(final BanqueDTO banqueDTO) {
        log.debug("Request to update Fournisseur : {}", banqueDTO);
        return BanqueDTO.fromEntity(
                banqueRepository.save(
                        BanqueDTO.toEntity(banqueDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<BanqueDTO> findAll() {
        log.debug("Request to get all Fournisseurs");
        return banqueRepository
                .findAll()
                .stream()
                .map(BanqueDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public BanqueDTO findOne(String id) {
        log.debug("Request to get TypeClientDTO : {}", id);
        return banqueRepository.findById(id)
                .map(BanqueDTO::fromEntity)
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
        banqueRepository.deleteById(id);
    }
}
