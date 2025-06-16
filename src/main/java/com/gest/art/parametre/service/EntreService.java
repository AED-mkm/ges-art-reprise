package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Entre;
import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.parametre.entite.Fournisseur;
import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.StockProduit;
import com.gest.art.parametre.entite.dto.EntreDTO;
import com.gest.art.parametre.entite.dto.EntreProduitDTO;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.entite.dto.MagasinDTO;
import com.gest.art.parametre.entite.exception.ProduitNotFoundException;
import com.gest.art.parametre.entite.mapper.EntreMapper;
import com.gest.art.parametre.repository.EntreProduitRepository;
import com.gest.art.parametre.repository.EntreRepository;
import com.gest.art.parametre.repository.FournisseurRepository;
import com.gest.art.parametre.repository.MagasinRepository;
import com.gest.art.parametre.repository.ProduitRepository;
import com.gest.art.parametre.repository.StockProduitRepository;
import com.gest.art.security.Utils.validator.EntreValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;



@Service
@Transactional
public class EntreService {
    private final Logger log = LoggerFactory.getLogger(EntreService.class);
    private final ProduitRepository produitRepository;
    private final MagasinRepository magasinRepository;
    private final FournisseurRepository fournisseurRepository;

    private final EntreProduitRepository entreProduitRepository;

    private final StockProduitRepository stockProduitRepository;
    private final StockService stockService;
    private final EntreRepository entreRepository;
    @Autowired
    private EntreMapper entreMapper;


    public EntreService(ProduitRepository produitRepository, MagasinRepository magasinRepository,
                        FournisseurRepository fournisseurRepository, EntreProduitRepository entreProduitRepository, StockProduitRepository stockProduitRepository, StockService stockService, EntreRepository entreRepository)
            throws ServiceException {
        this.produitRepository = produitRepository;

        this.magasinRepository = magasinRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.entreProduitRepository = entreProduitRepository;
        this.stockProduitRepository = stockProduitRepository;
        this.stockService = stockService;
        this.entreRepository = entreRepository;

    }

    @Transactional
    public EntreDTO save(final EntreDTO entreDTO) {

            // ===== 1. VALIDATION =====
            // Valide les champs obligatoires et les règles métier
            EntreValidator.validate(entreDTO);

            // ===== 2. CHARGEMENT DES ENTITÉS =====
            // Charge le magasin ou throw EntityNotFoundException
            final Magasin magasin = magasinRepository.findById(entreDTO.getMagasinId())
                    .orElseThrow(() -> new EntityNotFoundException
                            ("Magasin ID non trouvé"));

            // Charge le fournisseur ou throw EntityNotFoundException
            final Fournisseur fournisseur = fournisseurRepository.findById(entreDTO.getFournisseurId())
                    .orElseThrow(() -> new EntityNotFoundException
                            ("Fournisseur ID  non trouvé"));

        // ===== CRÉATION DE L'ENTRÉE =====
        Entre entre = EntreDTO.toEntity(entreDTO);
        entre.setMagasin(magasin);
        entreDTO.setNomMagasin(magasin.getNomMagasin());
        entre.setFournisseur(fournisseur);
        entreDTO.setNomFour(fournisseur.getNomFour());
        entreDTO.setObjet(entreDTO.getObjet());
        entreDTO.setNumBordLiv(entreDTO.getNumBordLiv());
        entreDTO.setDateEnt(LocalDate.now()); // Date système
        try{
                for (EntreProduitDTO ligneEndDTO : entreDTO.getEntreProduits()) {
                    Produit produit = produitRepository.findById(ligneEndDTO.getProduitId())
                            .orElseThrow(() -> new ProduitNotFoundException("Produit ID " + ligneEndDTO.getProduitId() + " introuvable."));

                    if (ligneEndDTO.getQuantite() == null || ligneEndDTO.getQuantite().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new ValidationException("La quantité du produit doit être positive.");
                    }

                    if (ligneEndDTO.getPrixEntre() == null || ligneEndDTO.getPrixEntre().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new ValidationException("Le prix d'entrée doit être positif");
                    }

                    EntreProduit entreProduit = EntreProduitDTO.toEntity(ligneEndDTO);
                    entreProduit.setProduit(produit);
                    ligneEndDTO.setCodeprod(produit.getCodeprod());
                    ligneEndDTO.setLibelle(produit.getLibelle());
                    entreProduit.setPrixEntre(ligneEndDTO.getPrixEntre());
                    entreProduit.setQuantite(ligneEndDTO.getQuantite());
                    entreProduit.setEntre(entre);
                    entreProduitRepository.save(entreProduit);

                    StockProduit stockProduit = stockProduitRepository.findByProduitId(produit.getId())
                            .orElseGet(() -> {
                                StockProduit sp = new StockProduit();
                                sp.setProduit(produit);
                                sp.setCoutAchat(BigDecimal.ZERO);
                                sp.setStockProduit(BigDecimal.ZERO); // Initialisation
                                return sp;
                            });
                    stockProduit.setStockProduit(stockProduit.getStockProduit().add(ligneEndDTO.getQuantite()));
                    stockProduit.setCoutAchat(ligneEndDTO.getPrixEntre());
                    stockProduit.setMagasin(entre.getMagasin());
                    stockProduitRepository.save(stockProduit);
                    stockProduitRepository.save(stockProduit);
                }

            Entre savedEntre = entreRepository.save(entre);
            return entreDTO;

        } catch (ProduitNotFoundException | ValidationException e) {
         log.warn("Échec de validation pour client ID {} : {}", entreDTO.getFournisseurId(), e.getMessage());
         throw e;
         } catch (Exception e) {
         log.error("Erreur lors de la création de la vente pour client ID {} : {}", entreDTO.getMagasinId(), e.getMessage(), e);
                    throw new ServiceException("Erreur lors de la création de la vente.", e);
         }

}

    public EntreDTO findOne(String id) {
        log.debug("Request to get Entre : {}", id);
        return entreRepository.findById(id)
                .map(EntreDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("non trouver"));
    }

    public void delete(final String id) {
        log.debug("Request to delete ligne de vente : {}", id);
        if (id == null) {
            log.info("L'id est null");
            return;
        }
        entreRepository.deleteById(id);
    }

    public Page<Entre> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return entreRepository.findAll(pageable);
    }

}