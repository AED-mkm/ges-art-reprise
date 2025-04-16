package com.gest.art.security.user;

import com.gest.art.security.repository.UserRepository;
import com.gest.art.security.user.dto.UserDTO;
import com.gest.art.security.user.dto.UserUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    private final UserRepository repository;

    public void changePassword(ChangePasswordRequest request, String id) {

        User user = repository.findByEmail(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        //var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public User save(User utilisateurDto) {
        User utilisateur = new User();
        utilisateur.setId(utilisateurDto.getId());
        utilisateur.setStatut(Statut.CLIENT);
        utilisateur.setPolice(utilisateurDto.getPolice());
        utilisateur.setUsername(utilisateurDto.getUsername());
        utilisateur.setEmail(utilisateurDto.getEmail());
        utilisateur.setNom(utilisateurDto.getNom());
        utilisateur.setPrenom(utilisateurDto.getPrenom());
        utilisateur.setTelephone(utilisateurDto.getTelephone());
        utilisateur.setDateDebut(utilisateurDto.getDateDebut());
        utilisateur.setDateFin(utilisateurDto.getDateFin());
        utilisateur.setEnabled(true);
        utilisateur.setPassword(passwordEncoder.encode(utilisateurDto.getPassword()));
        utilisateur.setRoles(utilisateurDto.getRoles());
        return repository.save(utilisateur);
    }

    public UserDTO saveUser(UserDTO utilisateurDto) {
        UserDTO utilisateur = new UserDTO();
        utilisateur.setId(utilisateurDto.getId());
        utilisateur.setStatut(Statut.AGENT);
        utilisateur.setPolice(utilisateurDto.getPolice());
        utilisateur.setUsername(utilisateurDto.getUsername());
        utilisateur.setEmail(utilisateurDto.getEmail());
        utilisateur.setNom(utilisateurDto.getNom());
        utilisateur.setPrenom(utilisateurDto.getPrenom());
        utilisateur.setTelephone(utilisateurDto.getTelephone());
        utilisateur.setDateDebut(utilisateurDto.getDateDebut());
        utilisateur.setDateFin(utilisateurDto.getDateFin());
        utilisateur.setEnabled(true);
        utilisateur.setPassword(passwordEncoder.encode(utilisateurDto.getPassword()));
        utilisateur.setRoleDTOS(utilisateurDto.getRoleDTOS());
        repository.save(UserDTO.toEntity(utilisateur));
        return utilisateur;
    }

    /**
     * Update userDTO dto.
     *
     * @param userDTO the userDTO dto
     *
     * @return the userDTO dto
     */
    public UserDTO update(UserUpdateDTO userDTO) {
        return UserUpdateDTO.fromEntity(repository.save(UserUpdateDTO.toEntity(userDTO)));
    }

    public User updateOne(User userDTO) {
        log.debug("Request to update UserDTO : {}", userDTO);
        return repository.save(userDTO);
    }

    public List<UserDTO> findAll() {
        return repository.findAll().stream().map(UserDTO::fromEntity).collect(Collectors.toList());

    }

    public UserDTO findById(String id) {
        return repository.findById(id).map(UserDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "aucune user trouvé" + id
                ));
    }

    public UserDTO findByEmail(String email) {
        if (!StringUtils.hasLength(email)) {
            log.error("Le champ email est vide");
            return null;
        }
        return UserDTO.fromEntity(repository.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException(
                        "aucune user trouvé" + email
                )));
    }

    public void delete(String id) {
        if (id == null) {
            log.error("Aucun user à supprimer");
        }
        assert id != null;
        repository.deleteById(id);
    }
}
