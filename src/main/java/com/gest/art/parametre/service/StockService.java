package com.gest.art.parametre.service;


import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.historique.HistoriquePrix;
import com.gest.art.parametre.repository.HistoriquePrixRepository;
import com.gest.art.parametre.repository.ProduitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



@Service
@Transactional
public class StockService {
	private final ProduitRepository produitRepository;
	private final HistoriquePrixRepository historiquePrixRepo;

	public StockService(ProduitRepository produitRepository, HistoriquePrixRepository historiquePrixRepo) {
		this.produitRepository = produitRepository;
		this.historiquePrixRepo = historiquePrixRepo;
	}

	public void mettreAJourStockEtPrix(Produit produit, BigDecimal quantite, BigDecimal nouveauPrix) {
		produit.setStockProduit(produit.getStockProduit().add(quantite));
		produit.setAncienPrix(produit.getPrixActuel());
		produit.setPrixActuel(nouveauPrix);
		HistoriquePrix historique = HistoriquePrix.builder()
				.produit(produit)
				.ancienPrix(produit.getAncienPrix())
				.nouveauPrix(nouveauPrix)
				.build();
		// 3. Sauvegarde avec vérification
		historiquePrixRepo.save(historique);
		produitRepository.save(produit);
	}

}
