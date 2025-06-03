package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.TypeClient;
import com.gest.art.parametre.entite.dto.BanqueDTO;
import com.gest.art.parametre.entite.dto.ClientDTO;
import com.gest.art.parametre.repository.ClientRepository;
import com.gest.art.parametre.repository.TypeClientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ClientService {
    private final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    private final TypeClientRepository typeClientRepository;

    public ClientService(ClientRepository clientRepository, TypeClientRepository typeClientRepository) {
        this.clientRepository = clientRepository;
        this.typeClientRepository = typeClientRepository;
    }


    /**
     * Save Fournisseur dto.
     *
     * @param clientDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public ClientDTO save(final ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        Client client = ClientDTO.toEntity(clientDTO);
        TypeClient typeClient = typeClientRepository.findById(clientDTO.getTypeClientDTO().getId())
                .orElseThrow(() -> new RuntimeException("TypeClient introuvable"));

        client.setTypeClient(typeClient);
        Client savedClient = clientRepository.save(client);
        return ClientDTO.fromEntity(savedClient);
    }

    /**
     * Update Fournisseur dto.
     *
     * @param clientDTO the Fournisseur dto
     * @return the Fournisseur dto
     */
    public ClientDTO update(final ClientDTO clientDTO) {
        log.debug("Request to update Fournisseur : {}", clientDTO);
        return ClientDTO.fromEntity(
                clientRepository.save(
                        ClientDTO.toEntity(clientDTO)));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        log.debug("Request to get all Fournisseurs");
        return clientRepository
                .findAll()
                .stream()
                .map(ClientDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ClientDTO findOne(String id) {
        log.debug("Request to get TypeClientDTO : {}", id);
        return clientRepository.findById(id)
                .map(ClientDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("non trouver"));
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(final String id) {
        log.debug("Request to delete Fournisseur : {}", id);
        if (id == null) {
            log.info("L'id est null");
            return;
        }
        clientRepository.deleteById(id);
    }

    public Page<Client> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return clientRepository.findAll(pageable);
    }
}
