package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Banque;
import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.Facture;
import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.Vente;
import com.gest.art.parametre.entite.dto.ClientDTO;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.entite.dto.VenteDTO;
import com.gest.art.parametre.entite.enums.TypeVente;
import com.gest.art.parametre.entite.exception.ProduitNotFoundException;
import com.gest.art.parametre.entite.mapper.LigneDeVenteMapper;
import com.gest.art.parametre.entite.mapper.VenteMapper;
import com.gest.art.parametre.repository.ClientRepository;
import com.gest.art.parametre.repository.FactureRepository;
import com.gest.art.parametre.repository.LigneDeVenteRepository;
import com.gest.art.parametre.repository.MagasinRepository;
import com.gest.art.parametre.repository.ProduitRepository;
import com.gest.art.parametre.repository.VenteRepository;
import com.gest.art.security.Utils.validator.VenteValidator;
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
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class VenteService {
    private final Logger log = LoggerFactory.getLogger(VenteService.class);
    private final ProduitRepository produitRepository;

    private final MagasinRepository magasinRepository;
    private final VenteRepository venteRepository;
    private final ClientRepository clientRepository;
    private final LigneDeVenteRepository ligneDeVenteRepository;

    private final FactureRepository factureRepository;

    @Autowired
    private NumeroService numeroService;

    @Autowired
    private VenteMapper venteMapper;

    @Autowired
    private LigneDeVenteMapper ligneDeVenteMapper;

    @Autowired
    private final StockService stockService;



    public VenteService(ProduitRepository produitRepository,
                        MagasinRepository magasinRepository, VenteRepository venteRepository, ClientRepository
                                clientRepository,
                        LigneDeVenteRepository ligneDeVenteRepository, FactureRepository factureRepository,
                        StockService stockService) {
        this.produitRepository = produitRepository;
        this.magasinRepository = magasinRepository;
        this.venteRepository = venteRepository;
        this.clientRepository = clientRepository;
        this.ligneDeVenteRepository = ligneDeVenteRepository;
        this.factureRepository = factureRepository;
        this.stockService = stockService;
    }

    @Transactional
    public VenteDTO createAndUpdate(final VenteDTO venteDTO) {
        VenteValidator.validate(venteDTO);

        // Chargement des entités nécessaires
        final Client client = clientRepository.findById(venteDTO.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client ID " + venteDTO.getClientId() + " non trouvé"));

        final Magasin magasin = magasinRepository.findById(venteDTO.getMagasinId())
                .orElseThrow(() -> new EntityNotFoundException("Magasin ID " + venteDTO.getMagasinId() + " non trouvé"));

        // Création de la vente
        Vente vente = venteMapper.toEntity(venteDTO);
        vente.setClient(client);
        vente.setMagasin(magasin);
        vente.setDateVente(LocalDate.now());
        vente.setTypeVente(TypeVente.F);
        venteRepository.save(vente);
        BigDecimal prixTotalVente = BigDecimal.ZERO;
        try {
            for (LigneDeVenteDTO ligneDTO : venteDTO.getLignesDeVenteIds()) {
                Produit produit = produitRepository.findById(ligneDTO.getProduitId())
                        .orElseThrow(() -> new ProduitNotFoundException("Produit ID " + ligneDTO.getProduitId() + " introuvable."));

                if (ligneDTO.getQteVente() == null || ligneDTO.getQteVente().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ValidationException("La quantité du produit doit être positive.");
                }

                if (ligneDTO.getQteVente().compareTo(produit.getStockProduit()) > 0) {
                    throw new ValidationException("La quantité saisie est supérieure à la quantité en stock.");
                }
                ligneDTO.setPrixUnitaire(produit.getPrixActuel());
                BigDecimal prixTotal = ligneDTO.getPrixUnitaire().multiply(ligneDTO.getQteVente());
                produit.setStockProduit(produit.getStockProduit().subtract(ligneDTO.getQteVente()));
                ligneDTO.setPrixTotal(prixTotal);
                produitRepository.save(produit);

               /* LigneDeVente ligne = ligneDeVenteMapper.toEntity(ligneDTO);
                ligne.setQteVente(ligneDTO.getQteVente());
                ligne.setPrixUnitaire(ligneDTO.getPrixUnitaire());
                ligneDTO.setPrixTotal(ligneDTO.getPrixUnitaire().multiply(ligneDTO.getQteVente()));
                ligne.setVente(vente);
                ligneDTO.setVenteId(ligneDTO.getVenteId());
                prixTotalVente = prixTotalVente.add(prixTotal);*/

                // Création de la ligne
                LigneDeVente ligne = new LigneDeVente();
                ligne.setProduit(produit);
                ligne.setQteVente(ligneDTO.getQteVente());
                ligne.setPrixUnitaire(ligneDTO.getPrixUnitaire());
                ligne.setVente(vente);
                ligne.setPrixTotal(prixTotal);
                prixTotalVente = prixTotalVente.add(prixTotal);
                ligneDeVenteRepository.save(ligne);
                log.info( " montant par ligne AAAAAAAAAA:"+prixTotal );
            }



            // Calcul du prix total HT
            BigDecimal prixTotalHT = prixTotalVente;
            vente.setMontantHt(prixTotalHT); // Toujours définir le montant HT
            // Application des taxes si sélectionnées
            if (venteDTO.getTaxesCochees() != null && !venteDTO.getTaxesCochees().isEmpty()) {
                try {
                    stockService.appliquerTaxesSurVente(venteDTO, vente, prixTotalHT);
                    log.info("Taxes appliquées avec succès pour la vente ID {}", vente.getId());
                } catch (Exception e) {
                    log.error("Erreur lors de l'application des taxes pour la vente ID {}: {}", vente.getId(), e.getMessage());
                    // Fallback: mettre les valeurs par défaut sans taxes
                    vente.setMontantTva(BigDecimal.ZERO);
                    vente.setMontantBic(BigDecimal.ZERO);
                    vente.setMontantTTC(prixTotalHT);
                }
            } else {
                // Aucune taxe sélectionnée
                vente.setMontantTva(BigDecimal.ZERO);
                vente.setMontantBic(BigDecimal.ZERO);
                vente.setMontantTTC(prixTotalHT);
                log.info("Aucune taxe sélectionnée pour la vente ID {}", vente.getId());
            }

            // Création de la facture
            Facture facture = new Facture();
            facture.setClient(client);
            facture.setMagasin(magasin);
            facture.setDateFacture(LocalDate.now());
            facture.setNumFacture(numeroService.generateFactureNumber(venteDTO.getMagasinId()));
            factureRepository.save(facture);
            vente.setObjet("FAC " + facture.getNumFacture());
            vente.setFactureId(facture.getId());

            // Mise à jour de l'objet Vente

            vente.setMontantTva(vente.getMontantTva());
            vente.setMontantBic(vente.getMontantBic());
            vente.setMontantTTC(vente.getMontantTTC());
            venteRepository.save(vente);
            return venteDTO;

        } catch (ProduitNotFoundException | ValidationException e) {
            log.warn("Échec de validation pour client ID {} : {}", venteDTO.getClientId(), e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Erreur lors de la création de la vente pour client ID {} : {}", venteDTO.getClientId(), e.getMessage(), e);
            throw new ServiceException("Erreur lors de la création de la vente.", e);
        }
    }

    public void ajouterProduitAVente(VenteDTO venteDTO, String produitId, BigDecimal quantite) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new ProduitNotFoundException("Produit non trouvé"));

        // Vérifier si le produit existe déjà dans les lignes
        Optional<LigneDeVenteDTO> ligneExistante = venteDTO.getLignesDeVenteIds().stream()
                .filter(l -> l.getProduitId().equals(produitId))
                .findFirst();

        if (ligneExistante.isPresent()) {
            // Mise à jour de la quantité si le produit existe déjà
            LigneDeVenteDTO ligne = ligneExistante.get();
            BigDecimal nouvelleQte = ligne.getQteVente().add(quantite);
            ligne.setQteVente(nouvelleQte);
            ligne.setPrixTotal(ligne.getPrixUnitaire().multiply(nouvelleQte));
        } else {

            // Création d'une nouvelle ligne

            LigneDeVenteDTO nouvelleLigne = new LigneDeVenteDTO();
            nouvelleLigne.setProduitId(produit.getId());
            nouvelleLigne.setCodeprod(produit.getCodeprod()); // Remplissage des infos produit
            nouvelleLigne.setLibelle(produit.getLibelle());
            nouvelleLigne.setQteVente(quantite);
            nouvelleLigne.setPrixUnitaire(produit.getPrixActuel());
            nouvelleLigne.setPrixTotal(produit.getPrixActuel().multiply(quantite));
            venteDTO.getLignesDeVenteIds().add(nouvelleLigne);
        }
    }

    public void retirerProduitDeVente(VenteDTO venteDTO, String produitId) {
        venteDTO.setLignesDeVenteIds(
                venteDTO.getLignesDeVenteIds().stream()
                        .filter(l -> !l.getProduitId().equals(produitId))
                        .collect( Collectors.toList())
        );
    }

    public void modifierQuantiteProduit(VenteDTO venteDTO, String produitId, BigDecimal nouvelleQuantite) {
        venteDTO.getLignesDeVenteIds().stream()
                .filter(l -> l.getProduitId().equals(produitId))
                .findFirst()
                .ifPresent(l -> {
                    l.setQteVente(nouvelleQuantite);
                    l.setPrixTotal(l.getPrixUnitaire().multiply(nouvelleQuantite));
                });
    }


    public VenteDTO getVenteById(String venteId) {
        log.debug("Request to get TypeClientDTO : {}", venteId);
        return venteRepository.findById(venteId)
                .map( VenteDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("non trouver"));
    }


    public Page<Vente> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return venteRepository.findAll(pageable);
    }


}