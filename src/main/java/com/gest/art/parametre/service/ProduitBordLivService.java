package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.ProduitBordLiv;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.entite.dto.ProduitBordLivDTO;
import com.gest.art.parametre.repository.ProduitBordLivRepository;
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
public class ProduitBordLivService {


	private final ProduitBordLivRepository produitBordLivRepository;

	public ProduitBordLivService(ProduitBordLivRepository produitBordLivRepository) {
		this.produitBordLivRepository = produitBordLivRepository;
	}


	public ProduitBordLivDTO save(final ProduitBordLivDTO dto) {
		log.debug("Request to save produitBord : {}", dto);
		return ProduitBordLivDTO.fromEntity(
				produitBordLivRepository.save(
						ProduitBordLivDTO.toEntity(dto)));
	}

	public ProduitBordLivDTO update(final ProduitBordLivDTO dto) {
		log.debug("Request to update Fournisseur : {}", dto);
		return ProduitBordLivDTO.fromEntity(
				produitBordLivRepository.save(
						ProduitBordLivDTO.toEntity(dto)));
	}

	public ProduitBordLivDTO findOne(String id) {
		log.debug("Request to get ProduitBordLivDTO : {}", id);
		return produitBordLivRepository.findById(id)
				.map(ProduitBordLivDTO::fromEntity)
				.orElseThrow(() -> new EntityNotFoundException("non trouver"));
	}

	public void delete(final String id) {
		log.debug("Request to delete ligne de vente : {}", id);
		if (id == null) {
			log.info("L'id est null");
			return;
		}
		produitBordLivRepository.deleteById(id);
	}

	public Page<ProduitBordLiv> findPage(final int pageNo, final int pageSize, final String sortBy) {
		Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		return produitBordLivRepository.findAll(pageable);
	}


}
