package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.Operation;
import com.gest.art.parametre.entite.Succursale;
import com.gest.art.parametre.entite.dto.OperationDTO;
import com.gest.art.parametre.repository.MagasinRepository;
import com.gest.art.parametre.repository.OperationRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class OperationService {
    private final Logger log = LoggerFactory.getLogger( OperationService.class);
    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;

    }


    /**
     * Save Fournisseur dto.
     *
     * @param operationDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public OperationDTO save(final OperationDTO operationDTO) {
        log.debug("Request to save Fournisseur : {}", operationDTO);
        return OperationDTO.fromEntity(
                operationRepository.save(
                        OperationDTO.toEntity(operationDTO)));
    }

    /**
     * Update Fournisseur dto.
     *
     * @param operationDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public OperationDTO update(final OperationDTO operationDTO) {
        log.debug("Request to update Fournisseur : {}", operationDTO);
        return OperationDTO.fromEntity(
                operationRepository.save(
                        OperationDTO.toEntity(operationDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<OperationDTO> findAll() {
        log.debug("Request to get all Fournisseurs");
        return operationRepository
                .findAll()
                .stream()
                .map(OperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public OperationDTO findOne(String id) {
        log.debug("Request to get TypeClientDTO : {}", id);
        return operationRepository.findById(id)
                .map(OperationDTO::fromEntity)
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
        operationRepository.deleteById(id);
    }


    public Page<Operation> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return operationRepository.findAll(pageable);
    }
}
