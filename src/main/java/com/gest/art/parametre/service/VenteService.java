package com.gest.art.parametre.service;

import com.gest.art.parametre.entite.Client;
import com.gest.art.parametre.entite.Entre;
import com.gest.art.parametre.entite.EntreProduit;
import com.gest.art.parametre.entite.Vente;
import com.gest.art.parametre.entite.dto.VenteDTO;
import com.gest.art.parametre.entite.enums.TypeVente;
import com.gest.art.parametre.entite.mapper.EntreMapper;
import com.gest.art.parametre.repository.ClientRepository;
import com.gest.art.parametre.repository.EntreRepository;
import com.gest.art.parametre.repository.MagasinRepository;
import com.gest.art.parametre.repository.ProduitRepository;
import com.gest.art.security.Utils.validator.VenteValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
public class VenteService {
    private final Logger log = LoggerFactory.getLogger(VenteService.class);
    private final ProduitRepository produitRepository;
    private final MagasinRepository magasinRepository;
    private final StockService stockService;

    private final ClientRepository clientRepository;




    public VenteService(ProduitRepository produitRepository, MagasinRepository magasinRepository,
                        StockService stockService, EntreRepository entreRepository, ClientRepository clientRepository)
            throws ServiceException {
        this.produitRepository = produitRepository;

        this.magasinRepository = magasinRepository;
        this.stockService = stockService;
        this.clientRepository = clientRepository;

    }


}