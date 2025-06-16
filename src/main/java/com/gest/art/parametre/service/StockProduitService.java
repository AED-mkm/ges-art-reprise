package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.StockProduit;
import com.gest.art.parametre.entite.TypeClient;
import com.gest.art.parametre.entite.dto.StockProduitDTO;
import com.gest.art.parametre.entite.dto.TypeClientDTO;
import com.gest.art.parametre.repository.StockProduitRepository;
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
public class StockProduitService {
    private final Logger log = LoggerFactory.getLogger( StockProduitService.class);
    private final StockProduitRepository stockProduitRepository;

    public StockProduitService(StockProduitRepository stockProduitRepository) {
        this.stockProduitRepository = stockProduitRepository;
    }


    /**
     * Save Fournisseur dto.
     *
     * @param stockProduitDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public StockProduitDTO save(final StockProduitDTO stockProduitDTO) {
        log.debug("Request to save Fournisseur : {}", stockProduitDTO);
        return StockProduitDTO.fromEntity(
                stockProduitRepository.save(
                        StockProduitDTO.toEntity(stockProduitDTO)));
    }

    /**
     * Update Fournisseur dto.
     *
     * @param stockProduitDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public StockProduitDTO update(final StockProduitDTO stockProduitDTO) {
        log.debug("Request to update Fournisseur : {}", stockProduitDTO);
        return StockProduitDTO.fromEntity(
                stockProduitRepository.save(
                        StockProduitDTO.toEntity(stockProduitDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<StockProduitDTO> findAll() {
        log.debug("Request to get all Fournisseurs");
        return stockProduitRepository
                .findAll()
                .stream()
                .map(StockProduitDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public StockProduitDTO findOne(String id) {
        log.debug("Request to get TypeClientDTO : {}", id);
        return stockProduitRepository.findById(id)
                .map(StockProduitDTO::fromEntity)
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
        stockProduitRepository.deleteById(id);
    }

    public Page<StockProduit> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return stockProduitRepository.findAll(pageable);
    }
}
