package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.dto.TypeClientDTO;
import com.gest.art.parametre.entite.dto.TypeReglementDTO;
import com.gest.art.parametre.repository.TypeReglRepository;
import com.gest.art.parametre.service.TypeReglementService;
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
public class TypeRegltResource {
    private static final String ENTITY_NAME = "Fournisseur";
    private final Logger log = LoggerFactory.getLogger(TypeRegltResource.class);
    private final TypeReglementService typeReglementService;

    private final TypeReglRepository typeReglRepository;

    public TypeRegltResource(TypeReglementService typeReglementService, TypeReglRepository typeReglRepository) {
        this.typeReglementService = typeReglementService;
        this.typeReglRepository = typeReglRepository;
    }


    /**
     * {@code POST  /Fournisseurs} : Create a new Fournisseur.
     *
     * @param typeReglementDTO the TypeClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the Fournisseur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/typereglements")
    public ResponseEntity<TypeReglementDTO> createFournisseur(@RequestBody final TypeReglementDTO typeReglementDTO) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", typeReglementDTO);
        if (typeReglementDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new Fournisseur cannot already have an ID");
        }
        TypeReglementDTO result = typeReglementService.save(typeReglementDTO);
        return ResponseEntity
                .created(new URI("/api/Fournisseurs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /Fournisseurs/:id} : Updates an existing Fournisseur.
     *
     * @param id       the id of the TypeClientDTO to save.
     * @param typeReglementDTO the TypeClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated FournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the FournisseurDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the FournisseurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/typereglements/{id}")
    public ResponseEntity<TypeReglementDTO> updateFournisseur(@PathVariable(value = "id", required = false) final String id, @RequestBody final TypeReglementDTO typeReglementDTO)
            throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}, {}", id, typeReglementDTO);
        if (typeReglementDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, typeReglementDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!typeReglRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        TypeReglementDTO result = typeReglementService.update(typeReglementDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeReglementDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Fournisseurs} : get all the Fournisseurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Fournisseurs in body.
     */
    @GetMapping("/typereglements")
    public List<TypeReglementDTO> getAllBanque() {
        log.debug("REST request to get all Fournisseurs");
        return typeReglementService.findAll();
    }

    /**
     * {@code GET  /Fournisseurs/:id} : get the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the FournisseurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/typereglements/{id}")
    public ResponseEntity<TypeReglementDTO> getFournisseur(@PathVariable final String id) {
        log.debug("REST request to get Fournisseur : {}", id);
        TypeReglementDTO typeReglementDTO = typeReglementService.findOne(id);
        return ResponseEntity.ok(typeReglementDTO);
    }

    /**
     * {@code DELETE  /Fournisseurs/:id} : delete the "id" Fournisseur.
     *
     * @param id the id of the FournisseurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/typereglements/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable final String id) {
        log.debug("REST request to delete Fournisseur : {}", id);
        typeReglementService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }
}
