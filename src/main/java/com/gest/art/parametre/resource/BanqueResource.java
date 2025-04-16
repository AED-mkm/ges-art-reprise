package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.dto.BanqueDTO;
import com.gest.art.parametre.entite.dto.FournisseurDTO;
import com.gest.art.parametre.repository.BanqueRepository;
import com.gest.art.parametre.service.BanqueService;
import com.gest.art.security.config.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BanqueResource {
    private static final String ENTITY_NAME = "Fournisseur";
    private final Logger log = LoggerFactory.getLogger(BanqueResource.class);
    private final BanqueService banqueService;

    private final BanqueRepository banqueRepository;

    public BanqueResource(BanqueService banqueService, BanqueRepository banqueRepository) {
        this.banqueService = banqueService;
        this.banqueRepository = banqueRepository;
    }


    /**
     * {@code POST  /Fournisseurs} : Create a new Fournisseur.
     *
     * @param BanqueDTO the FournisseurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the Fournisseur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/banques")
    public ResponseEntity<BanqueDTO> createFournisseur(@RequestBody final BanqueDTO BanqueDTO) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", BanqueDTO);
        if (BanqueDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new Fournisseur cannot already have an ID");
        }
        BanqueDTO result = banqueService.save(BanqueDTO);
        return ResponseEntity
                .created(new URI("/api/Fournisseurs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /Fournisseurs/:id} : Updates an existing Fournisseur.
     *
     * @param id       the id of the FournisseurDTO to save.
     * @param BanqueDTO the FournisseurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the FournisseurDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the FournisseurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/banques/{id}")
    public ResponseEntity<BanqueDTO> updateFournisseur(@PathVariable(value = "id", required = false) final String id, @RequestBody final BanqueDTO BanqueDTO)
            throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, BanqueDTO);
        if (BanqueDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, BanqueDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!banqueRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        BanqueDTO result = banqueService.update(BanqueDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, BanqueDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Fournisseurs} : get all the Fournisseurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Fournisseurs in body.
     */
    @GetMapping("/banques")
    public List<BanqueDTO> getAllBanque() {
        log.debug("REST request to get all Fournisseurs");
        return banqueService.findAll();
    }

    /**
     * {@code GET  /Fournisseurs/:id} : get the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the FournisseurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/banques/{id}")
    public ResponseEntity<BanqueDTO> getFournisseur(@PathVariable final String id) {
        log.debug("REST request to get Fournisseur : {}", id);
        BanqueDTO BanqueDTO = banqueService.findOne(id);
        return ResponseEntity.ok(BanqueDTO);
    }

    /**
     * {@code DELETE  /Fournisseurs/:id} : delete the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/banques/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable final String id) {
        log.debug("REST request to delete Fournisseur : {}", id);
        banqueService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }
}
