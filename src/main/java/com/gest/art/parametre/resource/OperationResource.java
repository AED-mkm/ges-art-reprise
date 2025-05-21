package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.Operation;
import com.gest.art.parametre.entite.Succursale;
import com.gest.art.parametre.entite.dto.OperationDTO;
import com.gest.art.parametre.entite.dto.SuccursaleDTO;
import com.gest.art.parametre.repository.OperationRepository;
import com.gest.art.parametre.service.OperationService;
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
public class OperationResource {
    private static final String ENTITY_NAME = "Operation";
    private final Logger log = LoggerFactory.getLogger( OperationResource.class);
    private final OperationRepository operationRepository;

    private final OperationService operationService;

    public OperationResource(OperationRepository operationRepository, OperationService operationService) {
        this.operationRepository = operationRepository;
        this.operationService = operationService;
    }


    /**
     * {@code POST  /Fournisseurs} : Create a new Fournisseur.
     *
     * @param operationDTO the TypeClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the Fournisseur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operations")
    public ResponseEntity<OperationDTO> createOperation(@RequestBody final OperationDTO operationDTO) throws URISyntaxException {
        log.debug("REST request to save Operation : {}", operationDTO);
        if (operationDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new Operation cannot already have an ID");
        }
        OperationDTO result = operationService.save(operationDTO);
        return ResponseEntity
                .created(new URI("/api/Operations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }


    @PutMapping("/operations/{id}")
    public ResponseEntity<OperationDTO> updateOperation(@PathVariable(value = "id", required = false)
                                           final String id, @RequestBody final OperationDTO operationDTO)
            throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, operationDTO);
        if (operationDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, operationDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!operationRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        OperationDTO result = operationService.update(operationDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, operationDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Fournisseurs} : get all the Fournisseurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Fournisseurs in body.
     */
    @GetMapping("/operations")
    public List<OperationDTO> getAllBanque() {
        log.debug("REST request to get all Fournisseurs");
        return operationService.findAll();
    }

    /**
     * {@code GET  /Fournisseurs/:id} : get the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the FournisseurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operations/{id}")
    public ResponseEntity<OperationDTO> getOperation(@PathVariable final String id) {
        log.debug("REST request to get Fournisseur : {}", id);
        OperationDTO operationDTO = operationService.findOne(id);
        return ResponseEntity.ok(operationDTO);
    }

    /**
     * {@code DELETE  /Fournisseurs/:id} : delete the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operations/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable final String id) {
        log.debug("REST request to delete Operation : {}", id);
        operationService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }

    @GetMapping("operations/pageAll")
    public ResponseEntity<Page<Operation>> allpage() {
        return new ResponseEntity<>(operationService.findPage(0, 5, "createdDate"), HttpStatus.OK);
    }
}
