package com.gest.art.security.user;

import com.gest.art.security.config.BadRequestAlertException;
import com.gest.art.security.config.HeaderUtil;
import com.gest.art.security.user.dto.RoleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@RestController
@RequestMapping("/api/v1/roles")
@CrossOrigin(origins = "*")
public class RoleResource {
    private final Logger log = LoggerFactory.getLogger(RoleResource.class);
    private final RoleService service;
    private String applicationName;

    public RoleResource(RoleService service) {
        this.service = service;
    }

    /**
     * @param roleDTO Création des roles
     * @return retourne un role au format json
     */
    @Operation(summary = "Création d'un role pour le rendre disponible dans l'application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role créé", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "identifiant de role invalide",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Role non créé", content = @Content)
    })
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Role createRole(@RequestBody Role roleDTO) {
        return service.save(roleDTO);
    }


    @Operation(summary = "CONSULTER LA LISTE DES ROLES POSSIBLE QU'UN UTILISATEUR PEUT AVOIR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des roles retournée avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun role à afficher",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Liste des roles non trouvée", content = @Content)
    })
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RoleDTO> listRole() {
        return service.findAll();
    }

    @Operation(summary = "Rechercher un role par son id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retourné avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun role à afficher",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun role trouvé", content = @Content)
    })
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleDTO findRoleById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleDTO findRoleByName(@PathVariable("name") String name) {
        return service.findByName(name);
    }

    @Operation(summary = "Modifier un role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role modifié avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun role à modifier",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun role non modifier", content = @Content)
    })
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Role updateRole(@PathVariable(value = "id", required = false) final String id, @RequestBody Role roleDTO) {
        log.debug("REST request to update role : {}", roleDTO);
        if (roleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalide id", ENTITY_NAME, "idnull");
        }
        roleDTO.setId(id);
        Role result = service.save(roleDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roleDTO.getId().toString()))
                .body(result).getBody();

    }

    @Operation(summary = "Supprimer un role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role supprimé aec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun role à supprimer",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun role trouvé", content = @Content)
    })
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") String id) {
        service.delete(id);
    }
}
