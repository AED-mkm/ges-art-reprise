package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.Entre;
import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.parametre.entite.Fournisseur;
import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.dto.EntreDTO;
import com.gest.art.parametre.entite.dto.EntreProduitDTO;
import com.gest.art.parametre.entite.mapper.EntreMapper;
import com.gest.art.parametre.repository.EntreRepository;
import com.gest.art.parametre.repository.FournisseurRepository;
import com.gest.art.parametre.repository.MagasinRepository;
import com.gest.art.parametre.repository.ProduitRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional
public class EntreService {
    private final Logger log = LoggerFactory.getLogger(EntreService.class);
    private final ProduitRepository produitRepository;
    private final MagasinRepository magasinRepository;
    private final FournisseurRepository fournisseurRepository;
    private final StockService stockService;
    private final EntreRepository entreRepository;
    @Autowired
    private EntreMapper entreMapper;


    public EntreService(ProduitRepository produitRepository, MagasinRepository magasinRepository,
                        FournisseurRepository fournisseurRepository, StockService stockService, EntreRepository entreRepository)
            throws ServiceException {
        this.produitRepository = produitRepository;

        this.magasinRepository = magasinRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.stockService = stockService;
        this.entreRepository = entreRepository;

    }

    @Transactional
    public EntreDTO save(final EntreDTO entreDTO) {
        try {
            // ===== 1. VALIDATION =====
            // Valide les champs obligatoires et les règles métier
            EntreValidator.validate(entreDTO);

            // ===== 2. CHARGEMENT DES ENTITÉS =====
            // Charge le magasin ou throw EntityNotFoundException
            final Magasin magasin = magasinRepository.findById(entreDTO.getMagasinId())
                    .orElseThrow(() -> new EntityNotFoundException
                            ("Magasin ID " + entreDTO.getMagasinId() + " non trouvé"));

            // Charge le fournisseur ou throw EntityNotFoundException
            final Fournisseur fournisseur = fournisseurRepository.findById(entreDTO.getFournisseurId())
                    .orElseThrow(() -> new EntityNotFoundException
                            ("Fournisseur ID " + entreDTO.getFournisseurId() + " non trouvé"));

            // ===== 3. CRÉATION DE L'ENTRÉE =====
            Entre entre = new Entre();
            entre.setMagasin(magasin);
            entre.setFournisseur(fournisseur);
            entre.setObjet(entreDTO.getObjet());
            entre.setNumBordLiv(entreDTO.getNumBordLiv());
            entre.setDateEnt(LocalDate.now()); // Date système

            // ===== 4. GESTION DES PRODUITS =====
            // Processus métier pour les produits
            List<EntreProduit> entreProduits = processProduits(entreDTO, entre);
            entre.setEntreProduits(entreProduits);

            // ===== 5. SAUVEGARDE =====
            // Sauvegarde en cascade (Entre + EntreProduit)
            Entre savedEntre = entreRepository.save(entre);

            // Conversion en DTO pour la réponse
            return entreMapper.toDTO(savedEntre);

        } catch (EntityNotFoundException | ValidationException e) {
            // On relance les exceptions métier telles quelles
            throw e;
        } catch (Exception e) {
            // Log et encapsulation des autres exceptions
            log.error("Erreur lors de la sauvegarde de l'entrée", e);
            throw new ServiceException("Erreur technique lors de la sauvegarde", e);
        }
    }

    /**
     * Traite la liste des produits associés à une entrée de stock.
     *
     * @param dto DTO contenant les informations des produits à traiter
     * @param entre L'entité Entre à laquelle lier les produits
     * @return Liste des EntreProduit créés
     * @throws EntityNotFoundException si un produit n'est pas trouvé en base
     */
    private List<EntreProduit> processProduits(EntreDTO dto, Entre entre) {
        // 1. Extraction des IDs des produits pour optimisation du chargement
        List<String> produitIds = dto.getEntreProduits().stream()
                .map(EntreProduitDTO::getProduitId)
                .distinct() // Évite les doublons
                .toList();

        // 2. Chargement batch des produits en une seule requête
        Map<String, Produit> produitsMap = produitRepository.findAllById(produitIds)
                .stream()
                .collect(Collectors.toMap(
                        Produit::getId,
                        Function.identity(),
                        (existing, replacement) -> existing // Gestion des doublons
                ));

        // 3. Traitement de chaque produit
        return dto.getEntreProduits().stream().map(epDTO -> {
            // 3.1 Vérification de l'existence du produit
            Produit produit = Optional.ofNullable(produitsMap.get(epDTO.getProduitId()))
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format("Produit [ID: %s] non trouvé", epDTO.getProduitId())
                    ));

            // 3.2 Mise à jour du stock et du prix via le service dédié
            stockService.mettreAJourStockEtPrix(
                    produit,
                    epDTO.getQuantite(),
                    epDTO.getPrixEntre()
            );

            // 3.3 Création de la liaison Entre-Produit
            EntreProduit entreProduit = EntreProduit.builder()
                    .produit(produit)
                    .entre(entre)
                    .quantite(epDTO.getQuantite())
                    .prixEntre(epDTO.getPrixEntre())
                    .build();
            // Validation des données avant retour
            validateEntreProduit(entreProduit);

            return entreProduit;
        }).toList();
    }

    /**
     * Valide les données d'un EntreProduit avant persistance.
     */
    private void validateEntreProduit(EntreProduit entreProduit) {
        if (entreProduit.getQuantite() == null || entreProduit.getQuantite().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("La quantité doit être positive");
        }
        if (entreProduit.getPrixEntre() == null || entreProduit.getPrixEntre().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Le prix d'entrée doit être positif");
        }
    }

    public Page<Entre> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return entreRepository.findAll(pageable);
    }

}