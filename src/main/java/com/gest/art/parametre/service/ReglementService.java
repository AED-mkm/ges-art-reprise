package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.BordereauLivraison;
import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.Reglement;
import com.gest.art.parametre.entite.TypeReglement;
import com.gest.art.parametre.entite.dto.BordereauReglementDTO;
import com.gest.art.parametre.entite.dto.DetailsBordereauDTO;
import com.gest.art.parametre.entite.dto.ReglementDTO;
import com.gest.art.parametre.entite.dto.ReglementGroupRequestDTO;
import com.gest.art.parametre.entite.dto.ReglementGroupResponseDTO;
import com.gest.art.parametre.entite.enums.EtatBordereau;
import com.gest.art.parametre.repository.BordereauRepository;
import com.gest.art.parametre.repository.ClientRepository;
import com.gest.art.parametre.repository.ReglementRepository;
import com.gest.art.parametre.repository.TypeReglRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ReglementService {
    private final Logger log = LoggerFactory.getLogger( ReglementService.class);
    private final ReglementRepository reglementRepository;
    private final BordereauRepository bordereauRepository;
    private final ClientRepository clientRepository;
    private final TypeReglRepository typeReglRepository;


    public ReglementService(ReglementRepository reglementRepository, BordereauRepository bordereauRepository, ClientRepository clientRepository, TypeReglRepository typeReglRepository) {
        this.reglementRepository = reglementRepository;
        this.bordereauRepository = bordereauRepository;
        this.clientRepository = clientRepository;
        this.typeReglRepository = typeReglRepository;
    }

    public ReglementGroupResponseDTO effectuerGroupeReglement(ReglementGroupRequestDTO reglementGroupRequestDTO) {

        Client client = validationClient(reglementGroupRequestDTO);
        // Création du règlement principal
        Reglement reglementPrincipal = createPrincipalReglement(reglementGroupRequestDTO, client);

        // Traitement des bordereaux
        List<BordereauReglementDTO> details = detailsBordereaux(reglementGroupRequestDTO, reglementPrincipal);

        // Calcul du montant restant total
        BigDecimal montantRestantTotal = calculateTotalRemaining(reglementGroupRequestDTO.getBordereauIds(),reglementGroupRequestDTO);
        log.info( "TOTAL MONTANT RESTANT:"+ montantRestantTotal );
        return buildResponse(reglementPrincipal, client, details, montantRestantTotal);

    }

    public Client validationClient(ReglementGroupRequestDTO reglementGroupRequestDTO){
        // verifier le client
        Client client = clientRepository.findById(reglementGroupRequestDTO.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));
        //Vérifier que les bordereaux appartiennent au client
        reglementGroupRequestDTO.getBordereauIds().forEach(
                bordereauId ->{
                    if(!bordereauRepository.existsByIdAndClientId(bordereauId,reglementGroupRequestDTO.getClientId())){
                        throw new IllegalArgumentException("Le bordereau " + bordereauId + " n'appartient pas au client");
                    }
                }
        );

        // Verifier si le montant est coherent
        BigDecimal totalImpayer = calculerTotalImpayer(reglementGroupRequestDTO.getBordereauIds());
        if(reglementGroupRequestDTO.getMontantTtc().compareTo(totalImpayer) > 0){
            throw new IllegalArgumentException("Le montant dépasse le total impayé");
        }
        return client;
    }

    private Reglement createPrincipalReglement (ReglementGroupRequestDTO reglementGroupRequestDTO, Client client){

        Reglement reglement = new Reglement();
        reglement.setDateRegl(LocalDate.now());
        reglement.setMontantRegl(reglementGroupRequestDTO.getMontantTtc());
        reglement.setLibelleRegl(reglementGroupRequestDTO.getLibelleRegl());
        reglement.setTypeReglement(typeReglRepository.findByCode(reglementGroupRequestDTO.getTypeReglementDTO().getCode()));
        reglement.setClient(client);
        return reglementRepository.save(reglement);
    }

    private List<BordereauReglementDTO> detailsBordereaux(ReglementGroupRequestDTO reglementGroupRequestDTO,
                                                          Reglement principalReglement){
        List<BordereauReglementDTO> details = new ArrayList<>();
        BigDecimal montantRestant = reglementGroupRequestDTO.getMontantTtc();
        for(String bordereauId:reglementGroupRequestDTO.getBordereauIds()){
            if(montantRestant.compareTo(BigDecimal.ZERO) <= 0) break;

            BordereauLivraison bordereauLivraison = bordereauRepository.findById(bordereauId)
                    .orElseThrow(() -> new EntityNotFoundException("Bordereau non trouvé: " + bordereauId));

            BigDecimal montantPayer  = reglementRepository.sumByBordereauId(bordereauId)
                    .orElse(BigDecimal.ZERO);
            BigDecimal impaye = bordereauLivraison.getNetApayer().subtract(montantPayer);
            BigDecimal montantRegl = BigDecimal.valueOf( Math.min(impaye.doubleValue(),montantRestant.doubleValue()) );

            if(montantRegl.compareTo(BigDecimal.ZERO) > 0){
                Reglement reglementDetail = new Reglement();
                reglementDetail.setNumRegl(principalReglement.getNumRegl() + "-" + (details.size() + 1));
                reglementDetail.setDateRegl(LocalDate.now());
                reglementDetail.setMontantRegl(montantRegl);
                reglementDetail.setTypeReglement(principalReglement.getTypeReglement());
                reglementDetail.setClient(principalReglement.getClient());
       /*       reglementDetail.set
                reglementDetail.setBordereau(bordereau);*/
                reglementRepository.save(reglementDetail);
                // Mettre à jour le bordereau
                miseAjourEtatBordereau(bordereauLivraison, montantPayer.add((montantRegl)));

                // Ajouter au détail
                details.add(new BordereauReglementDTO(
                        bordereauLivraison.getId(),
                        bordereauLivraison.getDateBordereau(),
                        bordereauLivraison.getNumBordereau(),
                        montantRegl,
                        impaye.subtract(montantRegl)));
                montantRestant = montantRestant.subtract(montantRegl);
            }
        }
        return details;
    }

    private void  miseAjourEtatBordereau(BordereauLivraison bordereauLivraison, BigDecimal totalReglement){
        if(totalReglement.compareTo(bordereauLivraison.getMontantTtc()) >= 0){
            bordereauLivraison.setEtatBordereau( EtatBordereau.SOLDE);
        } else if (totalReglement.compareTo(BigDecimal.ZERO) > 0) {
            bordereauLivraison.setEtatBordereau(EtatBordereau.NON_SOLDE);
        }
        bordereauLivraison.setMontantRestant(bordereauLivraison.getNetApayer().subtract(totalReglement));
        bordereauRepository.save(bordereauLivraison);
    }


    private BigDecimal calculerTotalImpayer(List<String> bordereauIds){
        return bordereauIds.stream()
                .map(bordereauId -> {
                    BordereauLivraison bordereauLivraison = bordereauRepository.findById(bordereauId)
                            .orElseThrow(() -> new EntityNotFoundException("Bordereau non trouvé"));
                    BigDecimal montantPayer  = reglementRepository.sumByBordereauId(bordereauId)
                            .orElse(BigDecimal.ZERO);
                    return bordereauLivraison.getNetApayer().subtract(bordereauLivraison.getMontantPayer());
                } )
                .reduce( BigDecimal.ZERO, BigDecimal::add );
    }

   private BigDecimal calculateTotalRemaining(List<String>bordereauIds, ReglementGroupRequestDTO reglementGroupRequestDTO){
        return calculerTotalImpayer(bordereauIds)
                .subtract(reglementGroupRequestDTO.getMontantTtc());
    }

    private ReglementGroupResponseDTO buildResponse(Reglement reglement, Client client
            , List<BordereauReglementDTO> details, BigDecimal montantRestantTotal) {
        return new ReglementGroupResponseDTO(
                reglement.getId(),
                reglement.getNumRegl(),
                reglement.getDateRegl(),
                reglement.getMontantRegl(),
                reglement.getTypeReglement().getTypeRegl(),
                client.getDenomination(),
                details,
                montantRestantTotal
        );
    }

}
