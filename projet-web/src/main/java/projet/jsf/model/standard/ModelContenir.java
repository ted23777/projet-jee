package projet.jsf.model.standard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoContenir;
import projet.commun.dto.DtoCulture;
import projet.commun.dto.DtoParcelle;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceContenir;
import projet.jsf.data.Contenir;
import projet.jsf.data.Culture;
import projet.jsf.data.Parcelle;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelContenir implements Serializable {

    private List<Contenir> liste;
    private Contenir courant;
    private List<Culture> culturesDisponibles;
    private List<Parcelle> parcellesDisponibles;
    
  //-------
 // Champs pour parcelleculture.xhtml
 //-------
 private Integer idParcelleSelectionnee;
 private Integer idCultureAAjouter;
 private Double partAAjouter;


    @EJB
    private IServiceContenir serviceContenir;

    @Inject
    private IMapper mapper;

    //-------
    // Getters
    //-------
    public List<Contenir> getListe() {
        if (liste == null) {
            liste = new ArrayList<>();
            for (DtoContenir dto : serviceContenir.listerTout()) {
                liste.add(mapper.map(dto));
            }
        }
        return liste;
    }

    public Contenir getCourant() {
        if (courant == null) {
            courant = new Contenir();
        }
        return courant;
    }

  //-------
 // Getters/Setters
 //-------
 public Integer getIdParcelleSelectionnee() {
     return idParcelleSelectionnee;
 }

 public void setIdParcelleSelectionnee(Integer idParcelleSelectionnee) {
     this.idParcelleSelectionnee = idParcelleSelectionnee;
 }

 public Integer getIdCultureAAjouter() {
     return idCultureAAjouter;
 }

 public void setIdCultureAAjouter(Integer idCultureAAjouter) {
     this.idCultureAAjouter = idCultureAAjouter;
 }

 public Double getPartAAjouter() {
     return partAAjouter;
 }

 public void setPartAAjouter(Double partAAjouter) {
     this.partAAjouter = partAAjouter;
 }
    //-------
    // Initialisations
    //-------
    public String actualiserCourant() {
        if (courant != null && courant.getIdContenir() != 0) {
            DtoContenir dto = serviceContenir.retrouver(courant.getIdContenir());
            if (dto == null) {
                UtilJsf.messageError("Association introuvable.");
                return "liste";
            } else {
                courant = mapper.map(dto);
            }
        }
        return null;
    }

    //-------
    // Actions
    //-------
    public String validerMiseAJour() {
        try {
            if (courant.getIdContenir() == 0) {
                serviceContenir.inserer(mapper.map(courant));
            } else {
                serviceContenir.modifier(mapper.map(courant));
            }
            UtilJsf.messageInfo("Mise à jour effectuée avec succès.");
            liste = null;
            return "liste";
        } catch (ExceptionValidation e) {
            UtilJsf.messageError(e);
            return null;
        }
    }

    public String supprimer(Contenir item) {
        try {
            serviceContenir.supprimer(item.getIdContenir());
            getListe().remove(item);
            UtilJsf.messageInfo("Suppression effectuée avec succès.");
        } catch (ExceptionValidation e) {
            UtilJsf.messageError(e);
        }
        return null;
    }

    //-------
    // Use cases MVP
    //-------
    public List<Culture> getCulturesDeParcelle(int idParcelle) {
        List<Culture> cultures = new ArrayList<>();
        for (DtoCulture dto : serviceContenir.listerCulturesDeParcelle(idParcelle)) {
            cultures.add(mapper.map(dto));
        }
        return cultures;
    }

    public List<Parcelle> getParcellesAvecCulture(int idCulture) {
        List<Parcelle> parcelles = new ArrayList<>();
        for (DtoParcelle dto : serviceContenir.listerParcellesAvecCulture(idCulture)) {
            parcelles.add(mapper.map(dto));
        }
        return parcelles;
    }

    //ERREUR AU NIVEAU DE L'INTERFACE DE IServiceContenir
    public Double getPartRestante(int idParcelle) {
        return IServiceContenir.getPartRestante(idParcelle);
    }

    public void ajouterCulture(int idParcelle, int idCulture, double part) {
        try {
            serviceContenir.ajouterCultureAParcelle(idParcelle, idCulture, part);
            UtilJsf.messageInfo("Culture ajoutée à la parcelle !");
            liste = null;
        } catch (ExceptionValidation e) {
            UtilJsf.messageError(e);
        }
    }
    
  //-------
 // Méthodes pour les vues
 //-------

 /**
  * Charge les données pour une parcelle
  */
 public void chargerParParcelle() {
     // Réinitialiser le formulaire
     idCultureAAjouter = null;
     partAAjouter = null;
 }

 /**
  * Récupère la somme des parts d'une parcelle
  */
 public Double getSommePartsParcelle(Integer idParcelle) {
     if (idParcelle == null) return 0.0;
     return serviceContenir.getSommePartsParcelle(idParcelle);
 }

 /**
  * Récupère la part d'une culture sur une parcelle
  */
 public Double getPartCulture(Integer idParcelle, Integer idCulture) {
     try {
         List<DtoContenir> liste = serviceContenir.listerParParcelle(idParcelle);
         for (DtoContenir dto : liste) {
             if (dto.getIdCulture().equals(idCulture)) {
                 return dto.getPart();
             }
         }
     } catch (Exception e) {
         // Ignorer
     }
     return 0.0;
 }

 /**
  * Ajoute une culture à la parcelle sélectionnée
  */
 public void ajouterCulturePourParcelle() {
     try {
         ajouterCulture(idParcelleSelectionnee, idCultureAAjouter, partAAjouter);
         idCultureAAjouter = null;
         partAAjouter = null;
     } catch (Exception e) {
         UtilJsf.messageError("Erreur : " + e.getMessage());
     }
 }

 /**
  * Retire une culture d'une parcelle
  */
 public void retirerCulture(Integer idParcelle, Integer idCulture) {
     try {
         // Trouver l'idContenir correspondant
         List<DtoContenir> liste = serviceContenir.listerParParcelle(idParcelle);
         for (DtoContenir dto : liste) {
             if (dto.getIdCulture().equals(idCulture)) {
                 serviceContenir.supprimer(dto.getIdContenir());
                 UtilJsf.messageInfo("Culture retirée avec succès");
                 return;
             }
         }
     } catch (ExceptionValidation e) {
         UtilJsf.messageError(e);
     }
 }
}
