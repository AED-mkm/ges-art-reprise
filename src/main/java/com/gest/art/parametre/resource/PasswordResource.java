package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.dto.PasswordDTO;
import com.gest.art.parametre.repository.PasswordRepository;
import com.gest.art.parametre.service.PasswordService;
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
public class PasswordResource {
    private static final String ENTITY_NAME = "PasswordResource";
    private final Logger log = LoggerFactory.getLogger(PasswordResource.class);
    private final PasswordService passwordService;

    private final PasswordRepository passwordRepository;

    public PasswordResource(PasswordService passwordService, PasswordRepository passwordRepository) {
        this.passwordService = passwordService;
        this.passwordRepository = passwordRepository;
    }


    /**
     * {@code POST  /Passwords} : Create a new passwords.
     *
     * @param PasswordDTO the PasswordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new PasswordDTO,
     * or with status {@code 400 (Bad Request)} if the passwords has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/passwords")
    public ResponseEntity<PasswordDTO> createpasswords(@RequestBody final PasswordDTO PasswordDTO) throws URISyntaxException {
        log.debug("REST request to save Password : {}", PasswordDTO);
        if (PasswordDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new Password cannot already have an ID");
        }
        PasswordDTO result = passwordService.save(PasswordDTO);
        return ResponseEntity
                .created(new URI("/api/passwords/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /Passwords/:id} : Updates an existing Password.
     *
     * @param id          the id of the PasswordDTO to save.
     * @param PasswordDTO the PasswordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated PasswordDTO,
     * or with status {@code 400 (Bad Request)} if the PasswordDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the PasswordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/passwords/{id}")
    public ResponseEntity<PasswordDTO> updatepasswords(@PathVariable(value = "id", required = false) final String id, @RequestBody PasswordDTO PasswordDTO)
            throws URISyntaxException {
        log.debug("REST request to update passwords : {}, {}", id, PasswordDTO);
        if (PasswordDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, PasswordDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!passwordRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        PasswordDTO result = passwordService.update(PasswordDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, PasswordDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Passwords} : get all the Passwords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Passwords in body.
     */
    @GetMapping("/passwords")
    public List<PasswordDTO> getAllPasswords() {
        log.debug("REST request to get all Passwords");
        return passwordService.findAll();
    }

    /**
     * {@code GET  /Passwords/:id} : get the "id" Password.
     *
     * @param id the id of the PasswordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PasswordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/passwords/{id}")
    public ResponseEntity<PasswordDTO> getpasswords(@PathVariable final String id) {
        log.debug("REST request to get passwords : {}", id);
        PasswordDTO PasswordDTO = passwordService.findOne(id);
        return ResponseEntity.ok(PasswordDTO);
    }

    /**
     * {@code DELETE  /Passwords/:id} : delete the "id" Password.
     *
     * @param id the id of the PasswordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/passwords/{id}")
    public ResponseEntity<Void> deletepasswords(@PathVariable final String id) {
        log.debug("REST request to delete passwords : {}", id);
        passwordService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }
}
