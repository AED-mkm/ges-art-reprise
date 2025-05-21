package com.gest.art.parametre.service;


import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.Taxe;
import com.gest.art.parametre.entite.Vente;
import com.gest.art.parametre.entite.dto.BordereauLivraisonDTO;
import com.gest.art.parametre.entite.dto.VenteDTO;
import com.gest.art.parametre.entite.historique.HistoriquePrix;
import com.gest.art.parametre.repository.HistoriquePrixRepository;
import com.gest.art.parametre.repository.ProduitRepository;
import com.gest.art.parametre.repository.TaxeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@Transactional
public class StockService {
	private final ProduitRepository produitRepository;
	private final HistoriquePrixRepository historiquePrixRepo;

	private final  TaxeRepository taxeRepository ;

	public StockService(ProduitRepository produitRepository, HistoriquePrixRepository historiquePrixRepo,
	                    TaxeRepository taxeRepository) {
		this.produitRepository = produitRepository;
		this.historiquePrixRepo = historiquePrixRepo;
		this.taxeRepository = taxeRepository;
	}

	/**
	 * Mettre à jour le stock
	 * @param produit
	 * @param quantite
	 * @param nouveauPrix
	 */

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

	/**
	 * Applique les taxes sélectionnées à une vente et met à jour le prix total
	 * @param dto DTO contenant les taxes sélectionnées
	 * @param vente Objet Vente à modifier
	 * @param prixTotalVente Prix total HT de la vente
	 * @throws IllegalArgumentException si les paramètres sont invalides
	 */
	public void appliquerTaxesSurVente(VenteDTO dto, Vente vente, BigDecimal prixTotalVente) {
		// Validation des paramètres
		if (dto == null || vente == null || prixTotalVente == null) {
			throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
		}

		if (prixTotalVente.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Le prix total ne peut pas être négatif");
		}

		final BigDecimal cent = new BigDecimal("100");
		BigDecimal montantTva = BigDecimal.ZERO;
		BigDecimal montantBic = BigDecimal.ZERO;
		BigDecimal prixTotalTTC = prixTotalVente;

		// Applique les taxes seulement si des taxes sont cochées
		if (dto.getTaxesCochees() != null && !dto.getTaxesCochees().isEmpty()) {
			List<Taxe> taxesApplicables = taxeRepository.findByCodeIn(dto.getTaxesCochees());

			// Pour suivre les taxes appliquées (pour audit)
			List<String> taxesAppliquees = new ArrayList<>();
			for (Taxe taxe : taxesApplicables) {
				BigDecimal taux = taxe.getTaxe().divide(cent, 4, RoundingMode.HALF_UP);
				String codeTaxe = taxe.getCode().toUpperCase();

				switch(codeTaxe) {
					case "01": // TVA
						montantTva = prixTotalVente.multiply(taux)
								.setScale(2, RoundingMode.HALF_UP);
						prixTotalTTC = prixTotalTTC.add(montantTva);
						taxesAppliquees.add("TVA (" + taxe.getTaxe() + "%)");
						break;

					case "02": // BIC
						montantBic = prixTotalVente.multiply(taux)
								.setScale(2, RoundingMode.HALF_UP);
						prixTotalTTC = prixTotalTTC.add(montantBic);
						taxesAppliquees.add("BIC (" + taxe.getTaxe() + "%)");
						break;

					default:
						log.warn("Taxe non gérée avec le code: {}", codeTaxe);
				}
			}

			// Mise à jour de l'objet Vente
			vente.setMontantTva(montantTva);
			vente.setMontantBic(montantBic);
			vente.setMontantTTC(prixTotalTTC);
			//vente.setTaxesAppliquees(String.join(", ", taxesAppliquees));
			log.info("Taxes appliquées: {}. Montant TVA: {}, BIC: {}, Total TTC: {}",
					taxesAppliquees, montantTva, montantBic, prixTotalTTC);
		} else {
			log.info("Aucune taxe sélectionnée, le prix TTC reste inchangé");
			vente.setMontantTTC(prixTotalVente);
		}
	}


	public void appliquerTaxesSurBordereau(BordereauLivraisonDTO dto, BordereauLivraison bord, BigDecimal prixTotalBord) {
		// Validation des paramètres
		if (dto == null || bord == null || prixTotalBord == null) {
			throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
		}

		if (prixTotalBord.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Le prix total ne peut pas être négatif");
		}

		final BigDecimal cent = new BigDecimal("100");
		BigDecimal montantTva = BigDecimal.ZERO;
		BigDecimal montantBic = BigDecimal.ZERO;
		BigDecimal prixTotalTTC = prixTotalBord;

		// Applique les taxes seulement si des taxes sont cochées
		if (dto.getTaxesCochees() != null && !dto.getTaxesCochees().isEmpty()) {
			List<Taxe> taxesApplicables = taxeRepository.findByCodeIn(dto.getTaxesCochees());

			// Pour suivre les taxes appliquées (pour audit)
			List<String> taxesAppliquees = new ArrayList<>();
			for (Taxe taxe : taxesApplicables) {
				BigDecimal taux = taxe.getTaxe().divide(cent, 4, RoundingMode.HALF_UP);
				String codeTaxe = taxe.getCode().toUpperCase();

				switch(codeTaxe) {
					case "01": // TVA
						montantTva = prixTotalBord.multiply(taux)
								.setScale(2, RoundingMode.HALF_UP);
						prixTotalTTC = prixTotalTTC.add(montantTva);
						taxesAppliquees.add("TVA (" + taxe.getTaxe() + "%)");
						bord.setTauxTva(taxe.getTaxe());
						break;

					case "02": // BIC
						montantBic = prixTotalBord.multiply(taux)
								.setScale(2, RoundingMode.HALF_UP);
						prixTotalTTC = prixTotalTTC.add(montantBic);
						taxesAppliquees.add("BIC (" + taxe.getTaxe() + "%)");
						 bord.setTauxBic(taxe.getTaxe());
						log.info( "TTTTTTTTTTT:"+ bord.getTauxBic());
						break;

					default:
						log.warn("Taxe non gérée avec le code: {}", codeTaxe);
				}

			}

			bord.setMontantTva(montantTva);
			bord.setMontantBic(montantBic);
			bord.setMontantTtc(prixTotalTTC);
			//vente.setTaxesAppliquees(String.join(", ", taxesAppliquees));
			log.info("Taxes appliquées: {}. Montant TVA: {}, BIC: {}, Total TTC: {}",
					taxesAppliquees, montantTva, montantBic, prixTotalTTC);
		} else {
			log.info("Aucune taxe sélectionnée, le prix TTC reste inchangé");
			bord.setMontantTtc(prixTotalBord);
		}
	}



}
