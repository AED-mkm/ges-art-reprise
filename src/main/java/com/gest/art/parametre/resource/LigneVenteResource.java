package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.dto.FournisseurDTO;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.repository.LigneDeVenteRepository;
import com.gest.art.parametre.service.LigneVenteService;
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
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class LigneVenteResource {
    private static final String ENTITY_NAME = "LigneDeVente";
    private final Logger log = LoggerFactory.getLogger( LigneVenteResource.class);
    private final LigneVenteService ligneVenteService;

    private final LigneDeVenteRepository ligneDeVenteRepository;

    public LigneVenteResource(LigneVenteService ligneVenteService, LigneDeVenteRepository ligneDeVenteRepository) {
        this.ligneVenteService = ligneVenteService;
        this.ligneDeVenteRepository = ligneDeVenteRepository;
    }



    @PostMapping("/ligneVentes")
    public ResponseEntity<LigneDeVenteDTO> createLigne(@RequestBody final LigneDeVenteDTO dto) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", dto);
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new lignevente cannot already have an ID");
        }
        LigneDeVenteDTO result = ligneVenteService.save(dto);
        return ResponseEntity
                .created(new URI("/api/ligneVentes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }


    @PutMapping("/ligneVentes/{id}")
    public ResponseEntity<LigneDeVenteDTO> updateLigne(@PathVariable(value = "id", required = false) final String id, @RequestBody final LigneDeVenteDTO dto)
            throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!ligneDeVenteRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        LigneDeVenteDTO result = ligneVenteService.update(dto);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dto.getId()))
                .body(result);
    }


    @GetMapping("/ligneVentes/{id}")
    public ResponseEntity<LigneDeVenteDTO> getLigne(@PathVariable final String id) {
        log.debug("REST request to get ligne : {}", id);
        LigneDeVenteDTO dto = ligneVenteService.findOne(id);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/ligneVentes/{id}")
    public ResponseEntity<Void> deleteLigne(@PathVariable final String id) {
        log.debug("REST request to delete ligne : {}", id);
        ligneVenteService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }

    @GetMapping("ligneVentes/pageAll")
    public ResponseEntity<Page<LigneDeVente>> allpage() {
        return new ResponseEntity<>(ligneVenteService.findPage(0, 5, "createdDate"), HttpStatus.OK);
    }
}
