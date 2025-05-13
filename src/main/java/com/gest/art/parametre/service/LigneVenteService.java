package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.dto.FournisseurDTO;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.repository.LigneDeVenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class LigneVenteService {


	private final LigneDeVenteRepository ligneDeVenteRepository;

	public LigneVenteService(LigneDeVenteRepository ligneDeVenteRepository) {
		this.ligneDeVenteRepository = ligneDeVenteRepository;
	}


	public LigneDeVenteDTO save(final LigneDeVenteDTO dto) {
		log.debug("Request to save Fournisseur : {}", dto);
		return LigneDeVenteDTO.fromEntity(
				ligneDeVenteRepository.save(
						LigneDeVenteDTO.toEntity(dto)));
	}

	public LigneDeVenteDTO update(final LigneDeVenteDTO dto) {
		log.debug("Request to update Fournisseur : {}", dto);
		return LigneDeVenteDTO.fromEntity(
				ligneDeVenteRepository.save(
						LigneDeVenteDTO.toEntity(dto)));
	}

	public LigneDeVenteDTO findOne(String id) {
		log.debug("Request to get LigneDeVenteDTO : {}", id);
		return ligneDeVenteRepository.findById(id)
				.map(LigneDeVenteDTO::fromEntity)
				.orElseThrow(() -> new EntityNotFoundException("non trouver"));
	}

	public void delete(final String id) {
		log.debug("Request to delete ligne de vente : {}", id);
		if (id == null) {
			log.info("L'id est null");
			return;
		}
		ligneDeVenteRepository.deleteById(id);
	}

	public Page<LigneDeVente> findPage(final int pageNo, final int pageSize, final String sortBy) {
		Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		return ligneDeVenteRepository.findAll(pageable);
	}


}
