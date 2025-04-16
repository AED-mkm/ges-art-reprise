package com.gest.art.security.user;

import com.gest.art.security.auth.AuthenticationResponse;
import com.gest.art.security.auth.AuthenticationService;
import com.gest.art.security.config.BadRequestAlertException;
import com.gest.art.security.config.HeaderUtil;
import com.gest.art.security.repository.UserRepository;
import com.gest.art.security.user.dto.UserDTO;
import com.gest.art.security.user.dto.UserUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserResource {
    private static final String ENTITY_NAME = "user";
    private final Logger log = LoggerFactory.getLogger(UserResource.class);
    private final UserService service;
    private final AuthenticationService serviceAuth;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private String applicationName;

    @Operation(summary = "Créer un UTILISATEUR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UTILISATEUR Créer avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun UTILISATEUR  Créer",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun UTILISATEUR non Créer", content = @Content)
    })
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User userDTO) throws Exception {
        User result = service.save(userDTO);
        return ResponseEntity.ok()
                // .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                .body(result).getBody();

    }

    @Operation(summary = "Créer un UTILISATEUR interne")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UTILISATEUR interne Créer avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun UTILISATEUR interne Créer",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun UTILISATEUR interne non Créer", content = @Content)
    })
    @PostMapping(value = "/users/interne", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createUserInterne(@RequestBody UserDTO userDTO) throws Exception {
        UserDTO result = UserDTO.fromEntity(userRepository.save(UserDTO.toEntity(userDTO)));
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                .body(result).getBody();

    }

    @PostMapping("/users/interne/interne")
    public ResponseEntity<AuthenticationResponse> registerInterene(@RequestBody UserDTO request) {
        return ResponseEntity.ok(serviceAuth.registerInterene(request));
    }

    @Operation(summary = "Modifier le mot de passe UTILISATEUR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "le mot de passe UTILISATEUR modifié avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun le mot de passe UTILISATEUR à modifier",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun UTILISATEUR modifier", content = @Content)
    })
    @PostMapping(value = "/users/changer-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser.getName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "CONSULTER LA LISTE DES UTILISATEUR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des UTILISATEUR retournée avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun UTILISATEUR à afficher",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Liste des UTILISATEURS non trouvée", content = @Content)
    })
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> listUsers() {
        return service.findAll();
    }

    @Operation(summary = "Rechercher un UTILISATEUR par son id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UTILISATEUR retourné avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun UTILISATEUR à afficher",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun UTILISATEUR trouvé", content = @Content)
    })
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO findUserById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @Operation(summary = "Modifier un UTILISATEUR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UTILISATEUR modifié avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun UTILISATEUR à modifier",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun UTILISATEUR non modifier", content = @Content)
    })
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id", required = false) final String id, @RequestBody UserUpdateDTO userDTO)
            throws URISyntaxException {
        log.debug("REST request to update userDTO : {}, {}", id, userDTO);
        if (userDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnull");
        }
        if (!Objects.equals(id, userDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idinvalid");
        }

        if (!userRepository.existsById(String.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "idnotfound");
        }

        UserDTO result = service.update(userDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    @PutMapping(value = "users/user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User update(@PathVariable(value = "id", required = false) final String id, @RequestBody User userDTO) {
        log.debug("REST request to update user : {}", userDTO);
        if (userDTO.getId() == null) {
            throw new BadRequestAlertException("Invalide id", ENTITY_NAME, "idnull");
        }
        userDTO.setId(id);
        User result = service.updateOne(userDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                .body(result).getBody();

    }

    @GetMapping("/users/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }


    @Operation(summary = "Supprimer un User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User supprimé aec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun User à supprimer",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun User trouvé", content = @Content)
    })
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        service.delete(id);
    }


    /**** RECHERCHER UN User PAR SON NOM ****/
    @Operation(summary = "Rechercher un User par son email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retourné avec succès ", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Aucun User à afficher",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Aucun User n'a été trouvé", content = @Content)
    })
    @GetMapping(value = "/users/user/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO findUserName(@PathVariable("email") String email) {
        return service.findByEmail(email);
    }
}
