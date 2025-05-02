package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.dto.FournisseurDTO;
import com.gest.art.parametre.repository.FournisseurRepository;
import com.gest.art.parametre.service.FournisseurService;
import com.gest.art.security.config.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class FournisseurResource {
    private static final String ENTITY_NAME = "Fournisseur";
    private final Logger log = LoggerFactory.getLogger(FournisseurResource.class);
    private final FournisseurService fournisseurService;

    private final FournisseurRepository fournisseurRepository;

    public FournisseurResource(FournisseurService fournisseurService,
                               FournisseurRepository fournisseurRepository) {
        this.fournisseurService = fournisseurService;
        this.fournisseurRepository = fournisseurRepository;
    }


    /**
     * {@code POST  /Fournisseurs} : Create a new Fournisseur.
     *
     * @param fournisseurDTO the FournisseurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the Fournisseur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fournisseurs")
    public ResponseEntity<FournisseurDTO> createFournisseur(@RequestBody final FournisseurDTO fournisseurDTO) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", fournisseurDTO);
        if (fournisseurDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new Fournisseur cannot already have an ID");
        }
        FournisseurDTO result = fournisseurService.save(fournisseurDTO);
        return ResponseEntity
                .created(new URI("/api/Fournisseurs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /Fournisseurs/:id} : Updates an existing Fournisseur.
     *
     * @param id       the id of the FournisseurDTO to save.
     * @param FournisseurDTO the FournisseurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the FournisseurDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the FournisseurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fournisseurs/{id}")
    public ResponseEntity<FournisseurDTO> updateFournisseur(@PathVariable(value = "id", required = false) final String id, @RequestBody final FournisseurDTO FournisseurDTO)
            throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, FournisseurDTO);
        if (FournisseurDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, FournisseurDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!fournisseurRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        FournisseurDTO result = fournisseurService.update(FournisseurDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, FournisseurDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Fournisseurs} : get all the Fournisseurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Fournisseurs in body.
     */
    @GetMapping("/fournisseurs")
    public List<FournisseurDTO> getAllFournisseurs() {
        log.debug("REST request to get all Fournisseurs");
        return fournisseurService.findAll();
    }

    /**
     * {@code GET  /Fournisseurs/:id} : get the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the FournisseurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fournisseurs/{id}")
    public ResponseEntity<FournisseurDTO> getFournisseur(@PathVariable final String id) {
        log.debug("REST request to get Fournisseur : {}", id);
        FournisseurDTO fournisseurDTO = fournisseurService.findOne(id);
        return ResponseEntity.ok(fournisseurDTO);
    }

    /**
     * {@code DELETE  /Fournisseurs/:id} : delete the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fournisseurs/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable final String id) {
        log.debug("REST request to delete Fournisseur : {}", id);
        fournisseurService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }
}
