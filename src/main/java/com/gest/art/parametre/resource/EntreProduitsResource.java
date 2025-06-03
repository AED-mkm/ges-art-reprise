package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.dto.EntreProduitDTO;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.repository.EntreProduitRepository;
import com.gest.art.parametre.service.EntreProduitService;
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
public class EntreProduitsResource {
    private static final String ENTITY_NAME = "EntreProduits";
    private final Logger log = LoggerFactory.getLogger(EntreProduitsResource.class);
    private final EntreProduitRepository entreProduitRepository;

    private final EntreProduitService entreProduitService;

    public EntreProduitsResource(EntreProduitRepository entreProduitRepository, EntreProduitService entreProduitService) {
        this.entreProduitRepository = entreProduitRepository;
        this.entreProduitService = entreProduitService;
    }

    @PostMapping("/entreProduits")
    public ResponseEntity<EntreProduitDTO> createLigne(@RequestBody final EntreProduitDTO dto) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", dto);
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new lignevente cannot already have an ID");
        }
        EntreProduitDTO result = entreProduitService.save(dto);
        return ResponseEntity
                .created(new URI("/api/entreProduits/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }


    @PutMapping("/entreProduits/{id}")
    public ResponseEntity<EntreProduitDTO> updateLigne(@PathVariable(value = "id", required = false) final String id, @RequestBody final EntreProduitDTO dto)
            throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!entreProduitRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        EntreProduitDTO result = entreProduitService.update(dto);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dto.getId()))
                .body(result);
    }


    @GetMapping("/entreProduits/{id}")
    public ResponseEntity<EntreProduitDTO> getLigne(@PathVariable final String id) {
        log.debug("REST request to get ligne : {}", id);
        EntreProduitDTO dto = entreProduitService.findOne(id);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/entreProduits/{id}")
    public ResponseEntity<Void> deleteLigne(@PathVariable final String id) {
        log.debug("REST request to delete ligne : {}", id);
        entreProduitService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }

    @GetMapping("entreProduits/pageAll")
    public ResponseEntity<Page<EntreProduit>> allpage() {
        return new ResponseEntity<>(entreProduitService.findPage(0, 5, "createdDate"), HttpStatus.OK);
    }
}
