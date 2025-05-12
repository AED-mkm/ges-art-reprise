package com.gest.art.parametre.service;



import com.gest.art.parametre.entite.NumeroFacture;
import com.gest.art.parametre.repository.NumeroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NumeroService {

	private final NumeroRepository numeroRepository;

	public String generateFactureNumber(String magasinId) {
		int currentYear = Year.now().getValue();
		// Récupère ou crée le compteur avec verrouillage pour éviter les conflits
		NumeroFacture counter = numeroRepository.findByMagasinIdAndYear(magasinId, currentYear)
				.orElseGet(() -> {
					NumeroFacture newCounter = new NumeroFacture();
					newCounter.setMagasinId(magasinId);
					newCounter.setYear(currentYear);
					newCounter.setLastNumber(0L);
					return numeroRepository.save(newCounter);
				});
		// Incrémente et sauvegarde
		counter.setLastNumber(counter.getLastNumber() + 1);
		numeroRepository.save(counter);
		// Format: AAAA-NNNNNNN
		return String.format("%d-%07d", currentYear, counter.getLastNumber());
	}
}
