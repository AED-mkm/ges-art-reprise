package com.gest.art.parametre.resource;


import com.gest.art.parametre.entite.StockProduit;
import com.gest.art.parametre.entite.dto.EntreProduitDTO;
import com.gest.art.parametre.entite.dto.StockProduitDTO;
import com.gest.art.parametre.repository.StockProduitRepository;
import com.gest.art.parametre.service.StockProduitService;
import com.gest.art.security.config.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class StockProduitResource {
    private static final String ENTITY_NAME = "StockProduit";
    private final Logger log = LoggerFactory.getLogger( StockProduitResource.class);
    @Value("${spring.application.name}")
    private String applicationName;

    private final StockProduitRepository stockProduitRepository;

    private final StockProduitService stockProduitService;

    public StockProduitResource(StockProduitRepository stockProduitRepository, StockProduitService stockProduitService) {
        this.stockProduitRepository = stockProduitRepository;
        this.stockProduitService = stockProduitService;
    }


    /**
     * {@code PUT  /Magasins/:id} : Updates an existing Magasin.
     *
     * @param id         the id of the MagasinDTO to save.
     * @param stockProduitDTO the MagasinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated MagasinDTO,
     * or with status {@code 400 (Bad Request)} if the MagasinDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the MagasinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("stockProduits/{id}")
    public ResponseEntity<StockProduitDTO> updateMagasin(@PathVariable(value = "id", required = false) final String id, @RequestBody final StockProduitDTO stockProduitDTO)
            throws URISyntaxException {
        log.debug("REST request to update Magasin : {}, {}", id, stockProduitDTO);
        if (stockProduitDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, stockProduitDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!stockProduitRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        StockProduitDTO result = stockProduitService.update(stockProduitDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockProduitDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Magasins} : get all the Magasins.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Magasins in body.
     */
    @GetMapping("stockProduits")
    public List<StockProduitDTO> getAllMagasins() {
        log.debug("REST request to get all Magasins");
        return stockProduitService.findAll();
    }

    /**
     * {@code GET  /Magasins/:id} : get the "id" Magasin.
     *
     * @param id the id of the MagasinDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the MagasinDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("stockProduits/{id}")
    public ResponseEntity<StockProduitDTO> getStockproduit(@PathVariable final String id) {
        log.debug("REST request to get Magasin : {}", id);
        StockProduitDTO MagasinDTO = stockProduitService.findOne(id);
        return ResponseEntity.ok(MagasinDTO);
    }

    /**
     * {@code DELETE  /Magasins/:id} : delete the "id" Magasin.
     *
     * @param id the id of the MagasinDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("stockProduits/{id}")
    public ResponseEntity<Void> deleteMagasin(@PathVariable final String id) {
        log.debug("REST request to delete Magasin : {}", id);
        stockProduitService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }

    @GetMapping("stockProduits/pageAll")
    public ResponseEntity<Page<StockProduit>> allpage() {
        return new ResponseEntity<>(stockProduitService.findPage(0, 5, "createdDate"), HttpStatus.OK);
    }

    /**
     * retourne stockproduit par magasinId
     * @param magasinId
     * @return List
     */
    @GetMapping("stockProduits/details/{magasinId}")
    public ResponseEntity<List<StockProduitDTO>>listStockProduitByMagasin(@PathVariable("magasinId") String magasinId) {
        List<StockProduitDTO> liste = stockProduitService.findByMagasinId(magasinId);
        return ResponseEntity.ok().body(liste);
    }

    /**
     * retourne le stockProduit par produitId
     * @param produitId
     * @return List
     */

    @GetMapping("stockProduits/details-produit/{produitId}")
    public ResponseEntity<List<StockProduitDTO>>listStockProduitByProduit(@PathVariable("produitId") String produitId) {
        List<StockProduitDTO> liste = stockProduitService.findAllByProduitId(produitId);
        return ResponseEntity.ok().body(liste);
    }



}
