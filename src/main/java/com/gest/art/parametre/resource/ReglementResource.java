package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.dto.BanqueDTO;
import com.gest.art.parametre.entite.dto.ReglementGroupRequestDTO;
import com.gest.art.parametre.entite.dto.ReglementGroupResponseDTO;
import com.gest.art.parametre.repository.BanqueRepository;
import com.gest.art.parametre.service.BanqueService;
import com.gest.art.parametre.service.ReglementService;
import com.gest.art.security.config.HeaderUtil;
import jakarta.validation.Valid;
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
public class ReglementResource {
    private static final String ENTITY_NAME = "Reglement";
    private final Logger log = LoggerFactory.getLogger( ReglementResource.class);
    private final ReglementService reglementService;

    public ReglementResource(ReglementService reglementService) {
        this.reglementService = reglementService;
    }

    @PostMapping("reglements")
    public ResponseEntity<ReglementGroupResponseDTO> createGroupReglement(
            @Valid @RequestBody ReglementGroupRequestDTO reglementGroupRequestDTO) {
        ReglementGroupResponseDTO response = reglementService.effectuerGroupeReglement(reglementGroupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

