package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.dto.ProduitDTO;
import com.gest.art.parametre.repository.ProduitRepository;
import com.gest.art.parametre.service.ProduitService;
import com.gest.art.security.config.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ProduitResource {
    private static final String ENTITY_NAME = "Produit";
    private final Logger log = LoggerFactory.getLogger(ProduitResource.class);
    private final ProduitService produitService;

    private final ProduitRepository produitRepository;

    public ProduitResource(ProduitService produitService, ProduitRepository produitRepository) {
        this.produitService = produitService;
        this.produitRepository = produitRepository;
    }

    /**
     * {@code POST  /Produits} : Create a new Produit.
     *
     * @param produitDTO the ProduitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ProduitDTO,
     * or with status {@code 400 (Bad Request)} if the Produit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/produits")
    public ResponseEntity<ProduitDTO> createProduit(@RequestBody final ProduitDTO produitDTO) throws URISyntaxException {
        log.debug("REST request to save Produit : {}", produitDTO);
        if (produitDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new Produit cannot already have an ID");
        }
        ProduitDTO result = produitService.save(produitDTO);
        return ResponseEntity
                .created(new URI("/api/Produits/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /Produits/:id} : Updates an existing Produit.
     *
     * @param id         the id of the ProduitDTO to save.
     * @param produitDTO the ProduitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ProduitDTO,
     * or with status {@code 400 (Bad Request)} if the ProduitDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the ProduitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/produits/{id}")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable(value = "id", required = false) final String id, @RequestBody final ProduitDTO produitDTO)
            throws URISyntaxException {
        log.debug("REST request to update Produit : {}, {}", id, produitDTO);
        if (produitDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, produitDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!produitRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        ProduitDTO result = produitService.update(produitDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, produitDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Produits} : get all the Produits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Produits in body.
     */
    @GetMapping("/produits")
    public List<ProduitDTO> getAllProduits() {
        log.debug("REST request to get all Produits");
        return produitService.findAll();
    }

    /**
     * {@code GET  /Produits/:id} : get the "id" Produit.
     *
     * @param id the id of the ProduitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ProduitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/produits/{id}")
    public ResponseEntity<ProduitDTO> getProduit(@PathVariable final String id) {
        log.debug("REST request to get Produit : {}", id);
        ProduitDTO produitDTO = produitService.findOne(id);
        return ResponseEntity.ok(produitDTO);
    }

    /**
     * {@code DELETE  /Produits/:id} : delete the "id" Produit.
     *
     * @param id the id of the ProduitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/produits/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable final String id) {
        log.debug("REST request to delete Produit : {}", id);
        produitService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }

    @GetMapping("produits/pageAll")
    public ResponseEntity<Page<Produit>> allpage() {
        return new ResponseEntity<>(produitService.findPage(0, 5, "createdDate"), HttpStatus.OK);
    }
}
