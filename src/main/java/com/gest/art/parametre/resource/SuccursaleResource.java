package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.Succursale;
import com.gest.art.parametre.entite.dto.SuccursaleDTO;
import com.gest.art.parametre.entite.dto.TaxeDTO;
import com.gest.art.parametre.repository.SuccursaleRepository;
import com.gest.art.parametre.service.SuccursaleService;
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
public class SuccursaleResource {
    private static final String ENTITY_NAME = "Fournisseur";
    private final Logger log = LoggerFactory.getLogger(SuccursaleResource.class);
    private final SuccursaleService succursaleService;

    private final SuccursaleRepository succursaleRepository;

    public SuccursaleResource(SuccursaleService succursaleService, SuccursaleRepository succursaleRepository) {
        this.succursaleService = succursaleService;
        this.succursaleRepository = succursaleRepository;
    }


    /**
     * {@code POST  /Fournisseurs} : Create a new Fournisseur.
     *
     * @param succursaleDTO the TypeClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the Fournisseur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/succursales")
    public ResponseEntity<SuccursaleDTO> createFournisseur(@RequestBody final SuccursaleDTO succursaleDTO) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", succursaleDTO);
        if (succursaleDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new Fournisseur cannot already have an ID");
        }
        SuccursaleDTO result = succursaleService.save(succursaleDTO);
        return ResponseEntity
                .created(new URI("/api/Fournisseurs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /Fournisseurs/:id} : Updates an existing Fournisseur.
     *
     * @param id       the id of the TypeClientDTO to save.
     * @param succursaleDTO the TypeClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the FournisseurDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the FournisseurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/succursales/{id}")
    public ResponseEntity<SuccursaleDTO> updateFournisseur(@PathVariable(value = "id", required = false) final String id, @RequestBody final SuccursaleDTO succursaleDTO)
            throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, succursaleDTO);
        if (succursaleDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, succursaleDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!succursaleRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        SuccursaleDTO result = succursaleService.update(succursaleDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, succursaleDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Fournisseurs} : get all the Fournisseurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Fournisseurs in body.
     */
    @GetMapping("/succursales")
    public List<SuccursaleDTO> getAllBanque() {
        log.debug("REST request to get all Fournisseurs");
        return succursaleService.findAll();
    }

    /**
     * {@code GET  /Fournisseurs/:id} : get the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the FournisseurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/succursales/{id}")
    public ResponseEntity<SuccursaleDTO> getFournisseur(@PathVariable final String id) {
        log.debug("REST request to get Fournisseur : {}", id);
        SuccursaleDTO succursaleDTO = succursaleService.findOne(id);
        return ResponseEntity.ok(succursaleDTO);
    }

    /**
     * {@code DELETE  /Fournisseurs/:id} : delete the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/succursales/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable final String id) {
        log.debug("REST request to delete Fournisseur : {}", id);
        succursaleService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }

    @GetMapping("succursales/pageAll")
    public ResponseEntity<Page<Succursale>> allpage() {
        return new ResponseEntity<>(succursaleService.findPage(0, 5, "createdDate"), HttpStatus.OK);
    }
}
