package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.ProduitBordLiv;
import com.gest.art.parametre.entite.StockProduit;
import com.gest.art.parametre.entite.dto.BordereauLivraisonDTO;
import com.gest.art.parametre.entite.dto.ClientDTO;
import com.gest.art.parametre.entite.dto.DetailsBordereauDTO;
import com.gest.art.parametre.entite.dto.MagasinDTO;
import com.gest.art.parametre.entite.dto.ProduitBordLivDTO;
import com.gest.art.parametre.entite.dto.ProduitDTO;
import com.gest.art.parametre.entite.enums.EtatBordereau;
import com.gest.art.parametre.entite.exception.ProduitNotFoundException;
import com.gest.art.parametre.entite.mapper.BordereauMapper;
import com.gest.art.parametre.entite.mapper.ProduitBordLivMapper;
import com.gest.art.parametre.repository.BordereauRepository;
import com.gest.art.parametre.repository.ClientRepository;
import com.gest.art.parametre.repository.MagasinRepository;
import com.gest.art.parametre.repository.ProduitBordLivRepository;
import com.gest.art.parametre.repository.ProduitRepository;
import com.gest.art.parametre.repository.StockProduitRepository;
import com.gest.art.security.Utils.validator.BorderearValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
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
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class BordereauService {
    private final Logger log = LoggerFactory.getLogger( BordereauService.class);
    private final ProduitBordLivRepository produitBordLivRepository;
    private final MagasinRepository magasinRepository;
    private final BordereauRepository bordereauRepository;
    private final ProduitRepository produitRepository;
    private final ClientRepository clientRepository;

    private final StockProduitRepository stockProduitRepository;

    @Autowired
    private NumeroService numeroService;

    @Autowired
    private BordereauMapper borderearMapper;

    @Autowired
    private ProduitBordLivMapper produitBordLivMapper;

    @Autowired
    private final StockService stockService;



    public BordereauService(ProduitRepository produitRepository,
                            ProduitBordLivRepository produitBordLivRepository, MagasinRepository magasinRepository, BordereauRepository bordereauRepository, ClientRepository
                                clientRepository,
                            StockProduitRepository stockProduitRepository, StockService stockService) {
        this.produitBordLivRepository = produitBordLivRepository;
        this.produitRepository = produitRepository;
        this.magasinRepository = magasinRepository;
        this.bordereauRepository = bordereauRepository;
        this.clientRepository = clientRepository;
        this.stockProduitRepository = stockProduitRepository;
        this.stockService = stockService;
    }

   /* @Transactional
    public BordereauLivraisonDTO createAndUpdate(final BordereauLivraisonDTO dto) {
        BorderearValidator.validate(dto);

        // Chargement des entités nécessaires
        final Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client ID " + dto.getClientId() + " non trouvé"));

        final Magasin magasin = magasinRepository.findById(dto.getMagasinId())
                .orElseThrow(() -> new EntityNotFoundException("Magasin ID " + dto.getMagasinId() + " non trouvé"));

        // Création du bordereau
        BordereauLivraison bord = borderearMapper.toEntity(dto);
        bord.setClient(client);
        bord.setMagasin(magasin);
        bord.setDateBordereau(LocalDate.now());
        //bord.setEtatBordereau(  );
        bordereauRepository.save(bord);
        BigDecimal prixTotalBord = BigDecimal.ZERO;
        try {
            for (ProduitBordLivDTO bordLivDTO : dto.getProduitBordLivs()) {
                Produit produit = produitRepository.findById(bordLivDTO.getProduitId())
                        .orElseThrow(() -> new ProduitNotFoundException("Produit ID " + bordLivDTO.getProduitId() + " introuvable."));

                if (bordLivDTO.getQteBordLiv() == null || bordLivDTO.getQteBordLiv().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ValidationException("La quantité du produit doit être positive.");
                }

                if (bordLivDTO.getQteBordLiv().compareTo(produit.getStockProduit()) > 0) {
                    throw new ValidationException("La quantité saisie est supérieure à la quantité en stock.");
                }
                bordLivDTO.setPrixAchatBordLiv(produit.getPrixActuel());
                BigDecimal prixTotal = bordLivDTO.getPrixAchatBordLiv().multiply(bordLivDTO.getQteBordLiv());
                produit.setStockProduit(produit.getStockProduit().subtract(bordLivDTO.getQteBordLiv()));
                bordLivDTO.setPrixBordLiv(prixTotal);
                produitRepository.save(produit);

                // Création de la ligne
                ProduitBordLiv ligneBord = new ProduitBordLiv();
                ligneBord.setProduit(produit);
                ligneBord.setQteBordLiv(bordLivDTO.getQteBordLiv());
                ligneBord.setPrixAchatBordLiv(bordLivDTO.getPrixAchatBordLiv());
                ligneBord.setBordereauLivraison(bord);
                ligneBord.setPrixBordLiv(prixTotal);
                prixTotalBord = prixTotalBord.add(prixTotal);
                produitBordLivRepository.save(ligneBord);
                log.info( " montant par ligne AAAAAAAAAA:"+prixTotal );
            }


            // Calcul du prix total HT
            BigDecimal prixTotalHT = prixTotalBord.add(bord.getTotalEmballage()
                    .add(bord.getTotalTransport()));
            bord.setTotalGeneral(prixTotalHT); // Toujours définir le montant HT
            // Application des taxes si sélectionnées
            if (dto.getTaxesCochees() != null && !dto.getTaxesCochees().isEmpty()) {
                try {
                    stockService.appliquerTaxesSurBordereau(dto, bord, prixTotalBord);
                    log.info("Taxes appliquées avec succès pour la vente ID {}", bord.getId());
                } catch (Exception e) {
                    log.error("Erreur lors de l'application des taxes pour la vente ID {}: {}", bord.getId(), e.getMessage());
                    // Fallback: mettre les valeurs par défaut sans taxes
                    bord.setMontantTva(BigDecimal.ZERO);
                    bord.setMontantBic(BigDecimal.ZERO);
                    bord.setTotalGeneral(prixTotalHT);
                }
            } else {
                // Aucune taxe sélectionnée
                bord.setMontantTva(BigDecimal.ZERO);
                bord.setMontantBic(BigDecimal.ZERO);
                bord.setTotalGeneral(prixTotalHT);
                log.info("Aucune taxe sélectionnée pour la vente ID {}", bord.getId());
            }

            // Mise à jour de l'objet Vente

            bord.setMontantTva(bord.getMontantTva());
            bord.setMontantBic(bord.getMontantBic());
            bord.setTotalGeneral(bord.getTotalGeneral());
            bordereauRepository.save(bord);
            return dto;

        } catch (ProduitNotFoundException | ValidationException e) {
            log.warn("Échec de validation pour client ID {} : {}", dto.getClientId(), e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Erreur lors de la création de la vente pour client ID {} : {}", dto.getClientId(), e.getMessage(), e);
            throw new ServiceException("Erreur lors de la création de la vente.", e);
        }
    }
*/


    @Transactional
    public BordereauLivraisonDTO createAndUpdate(final BordereauLivraisonDTO dto) {
        BorderearValidator.validate(dto);
        BordereauLivraison bord = creerBordereau(dto);
        creerBordereau(dto);
        traiterProduits(dto, bord);
        appliquerTaxesSiBesoin(dto, bord);
        dto.setDateBordereau(LocalDate.now());
        dto.setEtatBordereau(EtatBordereau.NON_SOLDE);
        dto.setNumBordereau(numeroService.generateBordereauNumber(dto.getMagasinDTO().getId())  );
        log.info( "AAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBB:"+dto);
        bordereauRepository.save(bord);
        return dto;
    }

    private BordereauLivraison creerBordereau(final BordereauLivraisonDTO dto) {
        Client client = clientRepository.findById(dto.getClientDTO().getId())
                .orElseThrow(() -> new EntityNotFoundException("Client ID " + dto.getClientDTO().getId() + " non trouvé"));

        Magasin magasin = magasinRepository.findById(dto.getMagasinDTO().getId())
                .orElseThrow(() -> new EntityNotFoundException("Magasin ID " + dto.getMagasinDTO().getId() + " non trouvé"));
        BordereauLivraison bordereau = BordereauLivraisonDTO.toEntity(dto);
        dto.setClientDTO( ClientDTO.fromEntity( client ) );
        dto.setMagasinDTO( MagasinDTO.fromEntity( magasin ) );
        bordereau.setDateBordereau(LocalDate.now());
        bordereau.setTotalTransport(dto.getTotalTransport());
        bordereau.setTotalEmballage(dto.getTotalEmballage());
        log.info( "AAAAAAAAAAAAAAAOOOOOOOOOO:"+dto.getMontantTtc());
        this.bordereauRepository.save(bordereau );
        return bordereau;
    }

    private void traiterProduits(BordereauLivraisonDTO dto, BordereauLivraison bord) {
        BigDecimal prixTotalBord = BigDecimal.ZERO;
        BigDecimal montantHt = BigDecimal.ZERO;
        BigDecimal frais = BigDecimal.ZERO;
        frais = dto.getTotalEmballage().add(dto.getTotalTransport());
        for (ProduitBordLivDTO bordLivDTO : dto.getProduitBordLivs()) {
            Produit produit = produitRepository.findById(bordLivDTO.getProduitDTO().getId())
                    .orElseThrow(() -> new ProduitNotFoundException("Produit ID " + bordLivDTO.getProduitDTO().getId() + " introuvable."));
            StockProduit stockProduit = stockProduitRepository.findByProduitId(produit.getId())
                    .orElseThrow(() -> new ProduitNotFoundException("Produit ID "));
            validerQuantiteProduit(bordLivDTO, stockProduit);
            BigDecimal prixTotal = calculerEtMettreAJourProduit(bordLivDTO, stockProduit);
            log.info("MMMMMMMMMMMMMMMMMMMMMMMMMMM PRIX TOTAL --------------: {}", prixTotal);
            creerProduitBordLiv(bordLivDTO, produit, stockProduit,bord, prixTotal);
            prixTotalBord = prixTotalBord.add(prixTotal);
            log.info("MMMMMMMMMMMMMMMMMMMMMMMMMMM PRIX TOTAL BORD--------------: {}", prixTotalBord);
        }
         montantHt = prixTotalBord.add(frais);
        log.info("MMMMMMMMMMMMMMMMM000000000000 MONTANT TTC --------------: {}", montantHt);
        dto.setMontantHT(montantHt);
        log.info("Montant par ligne Total Bord: {}", montantHt);
    }


    /**
     * Fonction de Création d'une Ligne de Produit
     * @param bordLivDTO
     * @param produit
     * @param bord
     * @param prixTotal
     */
    private void creerProduitBordLiv(ProduitBordLivDTO bordLivDTO, Produit produit, StockProduit stockProduit,
                                     BordereauLivraison bord, BigDecimal prixTotal) {
        ProduitBordLiv ligneBord = ProduitBordLivDTO.toEntity(bordLivDTO);
        bordLivDTO.setProduitDTO(ProduitDTO.fromEntity(produit));
        bordLivDTO.setStockProduit(stockProduit.getStockProduit());
        ligneBord.setQteBordLiv(bordLivDTO.getQteBordLiv());
        ligneBord.setPrixAchatBordLiv(bordLivDTO.getPrixAchatBordLiv());
        ligneBord.setBordereauLivraison(bord);
        produitBordLivRepository.save(ligneBord);
        log.info("Montant par ligne : {}", prixTotal);
    }

    /**
     * Fonction de Calcul et Mise à Jour du Produit
     * @param bordLivDTO
     * @param stockProduit
     * @return prixTotal
     */

    private BigDecimal calculerEtMettreAJourProduit(ProduitBordLivDTO bordLivDTO, StockProduit stockProduit) {
        BigDecimal prixTotal = bordLivDTO.getPrixAchatBordLiv().multiply(bordLivDTO.getQteBordLiv());
        stockProduit.setStockProduit(stockProduit.getStockProduit().subtract(bordLivDTO.getQteBordLiv()));
        stockProduitRepository.save(stockProduit);
        bordLivDTO.setPrixBordLiv(prixTotal);
        return prixTotal;
    }

    /**
     * Fonction de Validation de la Quantité
     * @param bordLivDTO
     * @param stockProduit
     */

    private void validerQuantiteProduit(ProduitBordLivDTO bordLivDTO, StockProduit stockProduit) {
        if (bordLivDTO.getQteBordLiv() == null || bordLivDTO.getQteBordLiv().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("La quantité du produit doit être positive.");
        }
        if (bordLivDTO.getQteBordLiv().compareTo(stockProduit.getStockProduit()) > 0) {
            throw new ValidationException("La quantité saisie est supérieure à la quantité en stock.");
        }
    }

    /**
     * Fonction d'Application des Taxes
     * @param dto
     * @param bord
     */

    private void appliquerTaxesSiBesoin(BordereauLivraisonDTO dto, BordereauLivraison bord) {
        BigDecimal prixTotalHT = dto.getMontantHT();
        if (dto.getTaxesCochees() != null && !dto.getTaxesCochees().isEmpty()) {
            try {
                stockService.appliquerTaxesSurBordereau(dto, bord, prixTotalHT.subtract(bord.getTotalEmballage())
                        .subtract(bord.getTotalTransport()));
                log.info("Taxes appliquées avec succès pour le bordereau ID {}", bord.getId());
            } catch (Exception e) {
                log.error("Erreur lors de l'application des taxes: {}", e.getMessage());
            }
           //  appliquerAucuneTaxe(bord, prixTotalHT);
        } else {
            appliquerAucuneTaxe(bord, prixTotalHT);
        }
        bord.setTauxBic(dto.getTauxBic());
        bord.setTauxTva(dto.getTauxTva());
        log.info("MMMMMMMMMMMMMMMMMMMMMMMMMMM MONTANT TTC: {}", dto.getNetApayer());

    }

    /**
     * Fonction pour Aucune Taxe
     * @param bord
     * @param prixTotalHT
     */

    private void appliquerAucuneTaxe(BordereauLivraison bord, BigDecimal prixTotalHT) {
        bord.setMontantTva(BigDecimal.ZERO);
        bord.setMontantBic(BigDecimal.ZERO);
        bord.setMontantTtc(prixTotalHT);
        log.info("Aucune taxe appliquée pour le bordereau ID {}", bord.getId());
    }


    public void ajouterProduitABordereau(BordereauLivraisonDTO dto, String produitId, BigDecimal quantite) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new ProduitNotFoundException("Produit non trouvé"));

        // Vérifier si le produit existe déjà dans les lignes
        Optional<ProduitBordLivDTO> ligneExistante = dto.getProduitBordLivs().stream()
                .filter(l -> l.getProduitId().equals(produitId))
                .findFirst();

        if (ligneExistante.isPresent()) {
            // Mise à jour de la quantité si le produit existe déjà
            ProduitBordLivDTO ligne = ligneExistante.get();
            BigDecimal nouvelleQte = ligne.getQteBordLiv().add(quantite);
            ligne.setQteBordLiv(nouvelleQte);
            ligne.setPrixBordLiv(ligne.getPrixAchatBordLiv().multiply(nouvelleQte));
        } else {

            // Création d'une nouvelle ligne
            ProduitBordLivDTO nouvelleLigne = new ProduitBordLivDTO();
            nouvelleLigne.setProduitId(produit.getId());
            nouvelleLigne.setCodeprod(produit.getCodeprod()); // Remplissage des infos produit
            nouvelleLigne.setLibelle(produit.getLibelle());
            nouvelleLigne.setQteBordLiv(quantite);
            nouvelleLigne.setPrixAchatBordLiv(produit.getPrixActuel());
            nouvelleLigne.setPrixBordLiv(produit.getPrixActuel().multiply(quantite));
            dto.getProduitBordLivs().add(nouvelleLigne);
        }
    }


    public BordereauLivraisonDTO getBotrdereauById(String bordId) {
        log.debug("Request to get BordereauLivraisonDTO : {}", bordId);
        return bordereauRepository.findById(bordId)
                .map( BordereauLivraisonDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("non trouver"));
    }


   public Page<BordereauLivraison> findPage(final int pageNo, final int pageSize, final String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return bordereauRepository.findAll(pageable);
    }


    public List<DetailsBordereauDTO>getBordereauxByClient(String clientId,EtatBordereau etatBordereau) {
        List<BordereauLivraison> bordereaux = bordereauRepository.getByClientIdAndEtatBordereau(clientId,etatBordereau);
        return bordereaux.stream()
                .map(DetailsBordereauDTO::new)
                .collect( Collectors.toList());
    }

}