package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.parametre.entite.Vente;
import com.gest.art.parametre.entite.dto.BordereauLivraisonDTO;
import com.gest.art.parametre.entite.dto.VenteDTO;
import com.gest.art.parametre.service.BordereauService;
import com.gest.art.security.config.HeaderUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class BordereauResource {
    private static final String ENTITY_NAME = "BordereauLivraison";
    private final Logger log = LoggerFactory.getLogger(BordereauResource.class);
    @Value("${spring.application.name}")
    private String applicationName;

    private final BordereauService bordereauService;

    public BordereauResource(BordereauService bordereauService) {
        this.bordereauService = bordereauService;
    }

    @PostMapping("/bordereaux")
    public ResponseEntity<BordereauLivraisonDTO> createBordereau(@Valid @RequestBody BordereauLivraisonDTO dto) throws URISyntaxException {
        log.debug("REST request to save Entre : {}", dto);
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new entre cannot already have an ID");
        }
        BordereauLivraisonDTO result = bordereauService.createAndUpdate(dto);
        return ResponseEntity
                .created(new URI("/api/v1/bordereaux/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    @GetMapping("bordereaux/pageAll")
    public ResponseEntity<Page<BordereauLivraison>> allpage() {
        return new ResponseEntity<>(bordereauService.findPage(0, 5, "createdDate"), HttpStatus.OK);
    }



}
