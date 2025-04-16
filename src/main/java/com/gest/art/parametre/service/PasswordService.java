package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.dto.PasswordDTO;
import com.gest.art.parametre.repository.PasswordRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PasswordService {
    private final Logger log = LoggerFactory.getLogger(PasswordService.class);
    private final PasswordRepository passwordRepository;

    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    /**
     * Save PasswordDTO dto.
     *
     * @param passwordDTO the PasswordDTO dto
     * @return the passwordDTO dto
     */
    public PasswordDTO save(final PasswordDTO passwordDTO) {
        log.debug("Request to save passwordDTO : {}", passwordDTO);

        return PasswordDTO.fromEntity(
                passwordRepository.save(
                        PasswordDTO.toEntity(passwordDTO)));
    }

    /**
     * Update PasswordDTO dto.
     *
     * @param passwordDTO the PasswordDTO dto
     * @return the passwordDTO dto
     */
    public PasswordDTO update(final PasswordDTO passwordDTO) {
        log.debug("Request to update passwordDTO : {}", passwordDTO);
        return PasswordDTO.fromEntity(
                passwordRepository.save(
                        PasswordDTO.toEntity(passwordDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PasswordDTO> findAll() {
        log.debug("Request to get all PasswordDTO");
        return passwordRepository
                .findAll()
                .stream()
                .map(PasswordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public PasswordDTO findOne(final String id) {
        log.debug("Request to get PasswordDTO : {}", id);
        return passwordRepository.findById(id)
                .map(PasswordDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("non trouver"));
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(final String id) {
        log.debug("Request to delete PasswordDTO : {}", id);
        if (id == null) {
            log.info("L'id est null");
            return;
        }
        passwordRepository.deleteById(String.valueOf(id));
    }
}
