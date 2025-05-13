package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.Succursale;
import com.gest.art.parametre.entite.dto.BanqueDTO;
import com.gest.art.parametre.entite.dto.SuccursaleDTO;
import com.gest.art.parametre.repository.SuccursaleRepository;
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
public class SuccursaleService {
    private final Logger log = LoggerFactory.getLogger(SuccursaleService.class);
    private final SuccursaleRepository succursaleRepository;
    private final BanqueService banqueService;



    public SuccursaleService(SuccursaleRepository succursaleRepository, BanqueService banqueService) {
        this.succursaleRepository = succursaleRepository;
        this.banqueService = banqueService;
    }

    /**
     * Save Fournisseur dto.
     *
     * @param succursaleDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public SuccursaleDTO save(final SuccursaleDTO succursaleDTO) {
        return SuccursaleDTO.fromEntity(succursaleRepository
                .save(SuccursaleDTO.toEntity(succursaleDTO)));
    }


    /**
     * Update Fournisseur dto.
     *
     * @param succursaleDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public SuccursaleDTO update(final SuccursaleDTO succursaleDTO) {
        log.debug("Request to update Fournisseur : {}", succursaleDTO);
        return SuccursaleDTO.fromEntity(
                succursaleRepository.save(
                        SuccursaleDTO.toEntity(succursaleDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<SuccursaleDTO> findAll() {
        log.debug("Request to get all Fournisseurs");
        return succursaleRepository
                .findAll()
                .stream()
                .map(SuccursaleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public SuccursaleDTO findOne(String id) {
        log.debug("Request to get TypeClientDTO : {}", id);
        return succursaleRepository.findById(id)
                .map(SuccursaleDTO::fromEntity)
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
        succursaleRepository.deleteById(id);
    }

    public Page<Succursale> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return succursaleRepository.findAll(pageable);
    }
}
