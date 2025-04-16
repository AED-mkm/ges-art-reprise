package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.dto.BanqueDTO;
import com.gest.art.parametre.entite.dto.TypeClientDTO;
import com.gest.art.parametre.repository.TypeClientRepository;
import com.gest.art.parametre.service.TypeClientService;
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
public class TypeClientResource {
    private static final String ENTITY_NAME = "Fournisseur";
    private final Logger log = LoggerFactory.getLogger(TypeClientResource.class);
    private final TypeClientService typeClientService;

    private final TypeClientRepository typeClientRepository;

    public TypeClientResource(TypeClientService typeClientService, TypeClientRepository typeClientRepository) {
        this.typeClientService = typeClientService;
        this.typeClientRepository = typeClientRepository;
    }


    /**
     * {@code POST  /Fournisseurs} : Create a new Fournisseur.
     *
     * @param typeClientDTO the TypeClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the Fournisseur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/typeclients")
    public ResponseEntity<TypeClientDTO> createFournisseur(@RequestBody final TypeClientDTO typeClientDTO) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", typeClientDTO);
        if (typeClientDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new Fournisseur cannot already have an ID");
        }
        TypeClientDTO result = typeClientService.save(typeClientDTO);
        return ResponseEntity
                .created(new URI("/api/Fournisseurs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /Fournisseurs/:id} : Updates an existing Fournisseur.
     *
     * @param id       the id of the TypeClientDTO to save.
     * @param typeClientDTO the TypeClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the FournisseurDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the FournisseurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/typeclients/{id}")
    public ResponseEntity<TypeClientDTO> updateFournisseur(@PathVariable(value = "id", required = false) final String id, @RequestBody final TypeClientDTO typeClientDTO)
            throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, typeClientDTO);
        if (typeClientDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, typeClientDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!typeClientRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        TypeClientDTO result = typeClientService.update(typeClientDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeClientDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Fournisseurs} : get all the Fournisseurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Fournisseurs in body.
     */
    @GetMapping("/typeclients")
    public List<TypeClientDTO> getAllBanque() {
        log.debug("REST request to get all Fournisseurs");
        return typeClientService.findAll();
    }

    /**
     * {@code GET  /Fournisseurs/:id} : get the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the FournisseurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/typeclients/{id}")
    public ResponseEntity<TypeClientDTO> getFournisseur(@PathVariable final String id) {
        log.debug("REST request to get Fournisseur : {}", id);
        TypeClientDTO typeClientDTO = typeClientService.findOne(id);
        return ResponseEntity.ok(typeClientDTO);
    }

    /**
     * {@code DELETE  /Fournisseurs/:id} : delete the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/typeclients/{id}")
    public ResponseEntity<Void> deleteTypeClient(@PathVariable final String id) {
        log.debug("REST request to delete Fournisseur : {}", id);
        typeClientService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }
}
