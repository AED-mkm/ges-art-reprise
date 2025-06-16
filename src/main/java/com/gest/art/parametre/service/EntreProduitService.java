package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.parametre.entite.dto.EntreProduitDTO;
import com.gest.art.parametre.repository.EntreProduitRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
public class EntreProduitService {


	private final EntreProduitRepository entreProduitRepository;

	public EntreProduitService(EntreProduitRepository entreProduitRepository) {
		this.entreProduitRepository = entreProduitRepository;
	}


	public EntreProduitDTO save(final EntreProduitDTO dto) {
		log.debug("Request to save Fournisseur : {}", dto);
		return EntreProduitDTO.fromEntity(
				entreProduitRepository.save(
						EntreProduitDTO.toEntity(dto)));
	}

	public EntreProduitDTO update(final EntreProduitDTO dto) {
		log.debug("Request to update Fournisseur : {}", dto);
		return EntreProduitDTO.fromEntity(
				entreProduitRepository.save(
						EntreProduitDTO.toEntity(dto)));
	}

	public EntreProduitDTO findOne(String id) {
		log.debug("Request to get LigneDeVenteDTO : {}", id);
		return entreProduitRepository.findById(id)
				.map(EntreProduitDTO::fromEntity)
				.orElseThrow(() -> new EntityNotFoundException("non trouver"));
	}

	public void delete(final String id) {
		log.debug("Request to delete ligne de vente : {}", id);
		if (id == null) {
			log.info("L'id est null");
			return;
		}
		entreProduitRepository.deleteById(id);
	}

	public Page<EntreProduit> findPage(final int pageNo, final int pageSize, final String sortBy) {
		Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		return entreProduitRepository.findAll(pageable);
	}

	public List<EntreProduitDTO>listEntreProduitByEntre(String entreId){
		return entreProduitRepository.findByEntreId(entreId)
				.stream()
				.map(EntreProduitDTO::fromEntity)
				.toList();
	}



}
