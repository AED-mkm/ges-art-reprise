package com.gest.art.parametre.resource;

import com.gest.art.parametre.entite.dto.EntreDTO;
import com.gest.art.parametre.entite.dto.VenteDTO;
import com.gest.art.parametre.service.VenteService;
import com.gest.art.security.config.HeaderUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class VenteResource {
    private static final String ENTITY_NAME = "Entre";
    private final Logger log = LoggerFactory.getLogger( VenteResource.class);
    @Value("${spring.application.name}")
    private String applicationName;

    private final VenteService venteService;

    public VenteResource(VenteService venteService) {
        this.venteService = venteService;
    }


    @PostMapping("/ventes")
    public ResponseEntity<VenteDTO> createEntre(@Valid @RequestBody VenteDTO venteDTO) throws URISyntaxException {
        log.debug("REST request to save Entre : {}", venteDTO);
        if (venteDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A new entre cannot already have an ID");
        }
        VenteDTO result = venteService.createAndUpdate(venteDTO);
        return ResponseEntity
                .created(new URI("/api/v1/ventes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     *  ajouter un produit à une vente
     * @param venteId
     * @param produitId
     * @param quantite
     * @return
     */

    @PostMapping("/ventes/{venteId}/ajouter-produit")
    public ResponseEntity<VenteDTO> ajouterProduit( @PathVariable String venteId, @RequestParam String produitId,
            @RequestParam BigDecimal quantite) {

        VenteDTO venteDTO = venteService.getVenteById(venteId);
        venteService.ajouterProduitAVente(venteDTO, produitId, quantite);
        // Sauvegarder les modifications
        VenteDTO updatedVente = venteService.createAndUpdate(venteDTO);
        return ResponseEntity.ok(updatedVente);
    }

    /**
     * retirer un produit d'une vente
     * @param venteId
     * @param produitId
     * @return
     */

    @PostMapping("/ventes/{venteId}/retirer-produit")
    public ResponseEntity<VenteDTO> retirerProduit( @PathVariable String venteId,  @RequestParam String produitId)
    {
        VenteDTO venteDTO = venteService.getVenteById(venteId);
        venteService.retirerProduitDeVente(venteDTO, produitId);
        // Sauvegarder les modifications
        VenteDTO updatedVente = venteService.createAndUpdate(venteDTO);
        return ResponseEntity.ok(updatedVente);
    }






}
