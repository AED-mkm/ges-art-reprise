package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.ProduitBordLiv;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.entite.dto.ProduitBordLivDTO;
import com.gest.art.parametre.repository.ProduitBordLivRepository;
import com.gest.art.parametre.service.ProduitBordLivService;
import com.gest.art.security.config.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ProduitBordLivResource {
    private static final String ENTITY_NAME = "ProduitBordLiv";
    private final Logger log = LoggerFactory.getLogger( ProduitBordLivResource.class);
    private final ProduitBordLivService produitBordLivService;

    private final ProduitBordLivRepository produitBordLivRepository;

    public ProduitBordLivResource(ProduitBordLivService produitBordLivService, ProduitBordLivRepository produitBordLivRepository) {
        this.produitBordLivService = produitBordLivService;
        this.produitBordLivRepository = produitBordLivRepository;
    }

    @PostMapping("/produitBordLivraison")
    public ResponseEntity<ProduitBordLivDTO> createLigne(@RequestBody final ProduitBordLivDTO dto) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", dto);
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new lignevente cannot already have an ID");
        }
        ProduitBordLivDTO result = produitBordLivService.save(dto);
        return ResponseEntity
                .created(new URI("/api/produitBordLivraison/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }


    @PutMapping("/produitBordLivraison/{id}")
    public ResponseEntity<ProduitBordLivDTO> updateLigne(@PathVariable(value = "id", required = false)
                                                             final String id, @RequestBody final ProduitBordLivDTO dto)
            throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!produitBordLivRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        ProduitBordLivDTO result = produitBordLivService.update(dto);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dto.getId()))
                .body(result);
    }


    @GetMapping("/produitBordLivraison/{id}")
    public ResponseEntity<ProduitBordLivDTO> getLigne(@PathVariable final String id) {
        log.debug("REST request to get ligne : {}", id);
        ProduitBordLivDTO dto = produitBordLivService.findOne(id);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/produitBordLivraison/{id}")
    public ResponseEntity<Void> deleteLigne(@PathVariable final String id) {
        log.debug("REST request to delete ligne : {}", id);
        produitBordLivService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }

    @GetMapping("produitBordLivraison/pageAll")
    public ResponseEntity<Page<ProduitBordLiv>> allpage() {
        return new ResponseEntity<>(produitBordLivService.findPage(0, 5, "createdDate"), HttpStatus.OK);
    }
}
