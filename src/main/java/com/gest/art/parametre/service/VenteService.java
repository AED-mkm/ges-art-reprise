package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.Facture;
import com.gest.art.parametre.entite.LigneDeVente;
import com.gest.art.parametre.entite.Magasin;
import com.gest.art.parametre.entite.Produit;
import com.gest.art.parametre.entite.StockProduit;
import com.gest.art.parametre.entite.Vente;
import com.gest.art.parametre.entite.dto.LigneDeVenteDTO;
import com.gest.art.parametre.entite.dto.ProduitDTO;
import com.gest.art.parametre.entite.dto.ReportFactureDTO;
import com.gest.art.parametre.entite.dto.VenteDTO;
import com.gest.art.parametre.entite.enums.TypeVente;
import com.gest.art.parametre.entite.exception.ProduitNotFoundException;
import com.gest.art.parametre.entite.mapper.VenteMapper;
import com.gest.art.parametre.repository.ClientRepository;
import com.gest.art.parametre.repository.FactureRepository;
import com.gest.art.parametre.repository.LigneDeVenteRepository;
import com.gest.art.parametre.repository.MagasinRepository;
import com.gest.art.parametre.repository.ProduitRepository;
import com.gest.art.parametre.repository.StockProduitRepository;
import com.gest.art.parametre.repository.VenteRepository;
import com.gest.art.security.Utils.validator.VenteValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final StockProduitRepository stockProduitRepository;
    private final FactureRepository factureRepository;
    @Autowired
    private NumeroService numeroService;
    @Autowired
    private VenteMapper venteMapper;

    @Autowired
    private final StockService stockService;



    public VenteService(ProduitRepository produitRepository,
                        MagasinRepository magasinRepository, VenteRepository venteRepository, ClientRepository
                                clientRepository,
                        LigneDeVenteRepository ligneDeVenteRepository, StockProduitRepository stockProduitRepository, FactureRepository factureRepository,
                        StockService stockService) {
        this.produitRepository = produitRepository;
        this.magasinRepository = magasinRepository;
        this.venteRepository = venteRepository;
        this.clientRepository = clientRepository;
        this.ligneDeVenteRepository = ligneDeVenteRepository;
        this.stockProduitRepository = stockProduitRepository;
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

        // Création de la
        Vente vente = VenteDTO.toEntity(venteDTO);
        vente.setClient(client);
        vente.setMagasin(magasin);
        vente.setDateVente(LocalDate.now());
        vente.setTypeVente(TypeVente.F);
        venteRepository.save(vente);
        BigDecimal prixTotalVente = BigDecimal.ZERO;
        try {
            for (LigneDeVenteDTO ligneDTO : venteDTO.getLignesDeVente()) {
                Produit produit = produitRepository.findById(ligneDTO.getProduitId())
                        .orElseThrow(() -> new ProduitNotFoundException("Produit ID " + ligneDTO.getProduitId() + " introuvable."));

                StockProduit stockProduit = stockProduitRepository.findByProduitId(produit.getId())
                        .orElseThrow(() -> new ProduitNotFoundException("Produit ID introuvable."));

                if (ligneDTO.getQteVente() == null || ligneDTO.getQteVente().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ValidationException("La quantité du produit doit être positive.");
                }

                if (ligneDTO.getQteVente().compareTo(stockProduit.getStockProduit()) > 0) {
                    throw new ValidationException("La quantité saisie est supérieure à la quantité en stock.");
                }
                ligneDTO.setPrixUnitaire(produit.getPrixActuel());
                BigDecimal prixTotal = ligneDTO.getPrixUnitaire().multiply(ligneDTO.getQteVente());
               //produit.setStockProduit(produit.getStockProduit().subtract(ligneDTO.getQteVente()));
                stockProduit.setStockProduit(stockProduit.getStockProduit().subtract(ligneDTO.getQteVente()));
                ligneDTO.setPrixTotal(prixTotal);
                stockProduitRepository.save(stockProduit);

                // Création de la ligne
                LigneDeVente ligneDeVente = LigneDeVenteDTO.toEntity(ligneDTO);
                ligneDeVente.setProduit(produit);
                ligneDTO.setCodeprod(produit.getCodeprod());
                ligneDTO.setLibelle(produit.getLibelle());
                ligneDeVente.setQteVente(ligneDeVente.getQteVente());
                ligneDeVente.setPrixUnitaire(ligneDeVente.getPrixUnitaire());
                ligneDeVente.setVente(vente);
                ligneDeVente.setPrixTotal(prixTotal);
                prixTotalVente = prixTotalVente.add(prixTotal);
                ligneDeVenteRepository.save(ligneDeVente);
                log.info( " montant par ligne AAAAAAAAAA:"+prixTotal );
            }


            // Calcul du prix total HT
            BigDecimal prixTotalHT = prixTotalVente;
            venteDTO.setMontantHt(prixTotalHT); // Toujours définir le montant HT
            // Application des taxes si sélectionnées
            if (venteDTO.getTaxesCochees() != null && !venteDTO.getTaxesCochees().isEmpty()) {
                try {
                    stockService.appliquerTaxesSurVente(venteDTO, vente, prixTotalHT);
                    log.info("Taxes appliquées avec succès pour la vente ID {}", vente.getId());
                } catch (Exception e) {
                    log.error("Erreur lors de l'application des taxes pour la vente ID {}: {}", vente.getId(), e.getMessage());
                    // Fallback: mettre les valeurs par défaut sans taxes
                    venteDTO.setMontantTva(BigDecimal.ZERO);
                    venteDTO.setMontantBic(BigDecimal.ZERO);
                    venteDTO.setMontantTTC(prixTotalHT);
                }
            } else {
                // Aucune taxe sélectionnée
                venteDTO.setMontantTva(BigDecimal.ZERO);
                venteDTO.setMontantBic(BigDecimal.ZERO);
                venteDTO.setMontantTTC(prixTotalHT);
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

            venteDTO.setMontantTva(vente.getMontantTva());
            venteDTO.setMontantBic(vente.getMontantBic());
            venteDTO.setMontantTTC(vente.getMontantTTC());
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

    public void ajouterProduitAVente(VenteDTO venteDTO, ProduitDTO produitDTO, BigDecimal quantite) {
        Produit produit = produitRepository.findById(produitDTO.getId())
                .orElseThrow(() -> new ProduitNotFoundException("Produit non trouvé"));

        // Vérifier si le produit existe déjà dans les lignes
        Optional<LigneDeVenteDTO> ligneExistante = venteDTO.getLignesDeVente().stream()
                .filter(l -> l.getProduitId().equals(produitDTO.getId()))
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
            //nouvelleLigne.setCodeprod(produit.getCodeprod()); // Remplissage des infos produit
            //nouvelleLigne.setLibelle(produit.getLibelle());
            nouvelleLigne.setQteVente(quantite);
            nouvelleLigne.setPrixUnitaire(produit.getPrixActuel());
            nouvelleLigne.setPrixTotal(produit.getPrixActuel().multiply(quantite));
            venteDTO.getLignesDeVente().add(nouvelleLigne);
        }
    }

    public void retirerProduitDeVente(VenteDTO venteDTO, ProduitDTO produitDTO) {
        venteDTO.setLignesDeVente(
                venteDTO.getLignesDeVente().stream()
                        .filter(l -> !l.getProduitId().equals(produitDTO.getId()))
                        .collect( Collectors.toList())
        );
    }

    public void modifierQuantiteProduit(VenteDTO venteDTO, ProduitDTO produitDTO, BigDecimal nouvelleQuantite) {
        venteDTO.getLignesDeVente().stream()
                .filter(l -> l.getProduitId().equals(produitDTO.getId()))
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

    private ReportFactureDTO builData (final VenteDTO vte){
        ReportFactureDTO dto = new ReportFactureDTO();
        Optional<Vente> vente = venteRepository.findById(dto.getVentId());
        Optional<Facture> facture = factureRepository.findById(dto.getFactureId());
        List<LigneDeVente> ligne = vente.get().getLignesDeVente();
        Produit produit = ligne.get(0).getProduit();

        dto.setDateVente(vte.getDateVente());
        dto.setNumFacture(facture.get().getNumFacture());
        dto.setCodeprod(produit.getCodeprod());
        dto.setLibelle(produit.getLibelle());
        dto.setQteVente(ligne.get(0).getQteVente());
        dto.setPrixUnitaire(ligne.get(0).getPrixUnitaire() );
        dto.setPrixTotal(ligne.get(0).getPrixTotal());
        dto.setMontantTTC(vente.get().getMontantTTC());
        return dto;
    }

    public ResponseEntity<byte[]> exportFacture(String ventId){
        Optional<Vente> vente = venteRepository.findById(ventId);
        Optional<Facture> facture = factureRepository.findById(vente.get().getFactureId());
        List<LigneDeVente> ligne = vente
                .map(Vente::getLignesDeVente)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone( ZoneOffset.UTC);
    try {
        InputStream in = getClass().getResourceAsStream("/reports/FACTURE.jrxml");
        JasperReport jaspertReport = JasperCompileManager.compileReport(in);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("numFacture", facture.get().getNumFacture());
     /*   parameters.put("codeprod",produit.getCodeprod());*/
       /* parameters.put("libelle", ligne.get(0).getProduit().getLibelle());*/
        parameters.put("qteVente", ligne.get(0).getQteVente());
        parameters.put("prixUnitaire", ligne.get(0).getPrixUnitaire());
        parameters.put("prixTotal", ligne.get(0).getPrixTotal());
        parameters.put("montantTTC", ligne.get(0).getVente().getMontantTTC());
        parameters.put("denomination", facture.get().getClient().getDenomination());
        parameters.put("magasin", facture.get().getMagasin().getNomMagasin());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jaspertReport, parameters, new JREmptyDataSource());
        log.info("FACTURE GENERE AVEC SUCCES");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "facture.pdf");
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        // exporter.exportReport();
        return new ResponseEntity<byte[]>( JasperExportManager.exportReportToPdf(jasperPrint), headers, HttpStatus.OK);
    } catch (JRException e) {
       // log.info("DATE DEBUT:" + diplome.getCandidat().getSession().getDebut());
        return new ResponseEntity<byte[]>( HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }


}