package com.gest.art.parametre.resource;
import com.gest.art.parametre.entite.dto.MagasinDTO;
import com.gest.art.parametre.repository.MagasinRepository;
import com.gest.art.parametre.service.MagasinService;
import com.gest.art.security.config.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class MagasinResource {
    private static final String ENTITY_NAME = "Magasin";
    private final Logger log = LoggerFactory.getLogger(MagasinResource.class);
    @Value("${spring.application.name}")
    private String applicationName;

    private final MagasinRepository magasinRepository;

    private final MagasinService magasinService;

    public MagasinResource(MagasinRepository magasinRepository, MagasinService magasinService) {
        this.magasinRepository = magasinRepository;
        this.magasinService = magasinService;
    }


    /**
     * {@code POST  /Magasins} : Create a new Magasin.
     *
     * @param magasinDTO the MagasinDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new MagasinDTO,
     * or with status {@code 400 (Bad Request)} if the Magasin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/magasins")
    public ResponseEntity<MagasinDTO> createMagasin(@RequestBody final MagasinDTO magasinDTO) throws URISyntaxException {
        log.debug("REST request to save Magasin : {}", magasinDTO);
        if (magasinDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new Magasin cannot already have an ID");
        }
        MagasinDTO result = magasinService.save(magasinDTO);
        return ResponseEntity
                .created(new URI("/api/v1/magasins/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /Magasins/:id} : Updates an existing Magasin.
     *
     * @param id         the id of the MagasinDTO to save.
     * @param magasinDTO the MagasinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated MagasinDTO,
     * or with status {@code 400 (Bad Request)} if the MagasinDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the MagasinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/magasins/{id}")
    public ResponseEntity<MagasinDTO> updateMagasin(@PathVariable(value = "id", required = false) final String id, @RequestBody final MagasinDTO magasinDTO)
            throws URISyntaxException {
        log.debug("REST request to update Magasin : {}, {}", id, magasinDTO);
        if (magasinDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, magasinDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!magasinRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        MagasinDTO result = magasinService.update(magasinDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, magasinDTO.getId()))
                .body(result);
    }

    /**
     * {@code GET  /Magasins} : get all the Magasins.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Magasins in body.
     */
    @GetMapping("/magasins")
    public List<MagasinDTO> getAllMagasins() {
        log.debug("REST request to get all Magasins");
        return magasinService.findAll();
    }

    /**
     * {@code GET  /Magasins/:id} : get the "id" Magasin.
     *
     * @param id the id of the MagasinDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the MagasinDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/magasins/{id}")
    public ResponseEntity<MagasinDTO> getMagasin(@PathVariable final String id) {
        log.debug("REST request to get Magasin : {}", id);
        MagasinDTO MagasinDTO = magasinService.findOne(id);
        return ResponseEntity.ok(MagasinDTO);
    }

    /**
     * {@code DELETE  /Magasins/:id} : delete the "id" Magasin.
     *
     * @param id the id of the MagasinDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/magasins/{id}")
    public ResponseEntity<Void> deleteMagasin(@PathVariable final String id) {
        log.debug("REST request to delete Magasin : {}", id);
        magasinService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id))
                .build();
    }


}
