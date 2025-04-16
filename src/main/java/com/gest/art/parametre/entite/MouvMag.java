package com.gest.art.parametre.entite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gest.art.security.auditing.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * @author Moctar
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "kg_mouv_mag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SQLDelete(sql = "update kg_mouv_mag set deleted = true where id=? ")
@Where(clause = "deleted=false")
public class MouvMag extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "num_vehi")
    private String numvehicule;
    @Column(name = "nom_chauf")
    private String nomChauffeur;
    @Column(name = "num_bord")
    private String numBordereau;
    @Column(name = "sens")
    private String sens;
    @Column(name = "type_blf")
    private String typeBlf;
    @Column(name = "date_mouv")
    private LocalDate dateMouv;
    @Column(name = "provenance")
    private String provenance;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "mouv_mag", allowSetters = true)
    private Facture facture;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bord_livr_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "mouv_mag", allowSetters = true)
    private BordereauLivraison bordereauLivraison;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mag_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "mouv_mag", allowSetters = true)
    private Magasin magasin;

}