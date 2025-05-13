package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.Entre;
import com.gest.art.parametre.entite.dto.EntreDTO;
import com.gest.art.parametre.entite.dto.MagasinDTO;
import com.gest.art.parametre.service.EntreService;
import com.gest.art.security.config.HeaderUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class EntreResource {
    private static final String ENTITY_NAME = "Entre";
    private final Logger log = LoggerFactory.getLogger(EntreResource.class);
    @Value("${spring.application.name}")
    private String applicationName;

    private final EntreService entreService;

    public EntreResource(EntreService entreService) {
        this.entreService = entreService;
    }


    /**
     * {@code POST  /Magasins} : Create a new Magasin.
     *
     * @param entreDTO the MagasinDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new MagasinDTO,
     * or with status {@code 400 (Bad Request)} if the Magasin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entres")
    public ResponseEntity<EntreDTO> createEntre(@Valid @RequestBody EntreDTO entreDTO) throws URISyntaxException {
        log.debug("REST request to save Entre : {}", entreDTO);
        if (entreDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new entre cannot already have an ID");
        }
        EntreDTO result = entreService.save(entreDTO);
        return ResponseEntity
                .created(new URI("/api/v1/entres/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("entres/pageAll")
    public ResponseEntity<Page<Entre>> allpage() {
        return new ResponseEntity<>(entreService.findPage(0, 5, "createdDate"), HttpStatus.OK);
    }


}
