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
import projet.commun.service.IServiceCulture;
import projet.commun.service.IServiceParcelle;
import projet.jsf.data.Contenir;
import projet.jsf.data.Culture;
import projet.jsf.data.Parcelle;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelContenir implements Serializable {

	//-------
    // Champs
    //-------
    
    private List<Contenir> liste;
    private Contenir courant;
    private Integer idParcelleFiltre;
    private Integer idCultureFiltre;
    
    // Listes pour les sélecteurs
    private List<Parcelle> listeParcelles;
    private List<Culture> listeCultures;
    
    @EJB
    private IServiceContenir serviceContenir;
    
    @EJB
    private IServiceParcelle serviceParcelle;
    
    @EJB
    private IServiceCulture serviceCulture;
    
    @Inject
    private IMapper mapper;
    
    //-------
    // Getters & Setters
    //-------
    
    public List<Contenir> getListe() {
        if (liste == null) {
            chargerListe();
        }
        return liste;
    }
    
    public Contenir getCourant() {
        if (courant == null) {
            courant = new Contenir();
        }
        return courant;
    }
    
    public void setCourant(Contenir courant) {
        this.courant = courant;
    }
    
    public Integer getIdParcelleFiltre() {
        return idParcelleFiltre;
    }
    
    public void setIdParcelleFiltre(Integer idParcelleFiltre) {
        this.idParcelleFiltre = idParcelleFiltre;
    }
    
    public Integer getIdCultureFiltre() {
        return idCultureFiltre;
    }
    
    public void setIdCultureFiltre(Integer idCultureFiltre) {
        this.idCultureFiltre = idCultureFiltre;
    }
    
    public List<Parcelle> getListeParcelles() {
        if (listeParcelles == null) {
            chargerListeParcelles();
        }
        return listeParcelles;
    }
    
    public List<Culture> getListeCultures() {
        if (listeCultures == null) {
            chargerListeCultures();
        }
        return listeCultures;
    }
    
    //------------------
    // Méthodes internes
    //------------------
    
    /**
     * Charge la liste selon les filtres
     */
    private void chargerListe() {
        try {
            liste = new ArrayList<>();
            List<DtoContenir> dtos;
            
            if (idParcelleFiltre != null) {
                dtos = serviceContenir.listerParParcelle(idParcelleFiltre);
            } else if (idCultureFiltre != null) {
                dtos = serviceContenir.listerParCulture(idCultureFiltre);
            } else {
                dtos = serviceContenir.listerTout();
            }
            
            for (DtoContenir dto : dtos) {
                liste.add(mapper.map(dto));
            }
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors du chargement : " + e.getMessage());
            liste = new ArrayList<>();
        }
    }
    
    /**
     * Charge la liste des parcelles pour le sélecteur
     */
    private void chargerListeParcelles() {
        try {
            listeParcelles = new ArrayList<>();
            List<DtoParcelle> dtos = serviceParcelle.listerTout();
            for (DtoParcelle dto : dtos) {
                listeParcelles.add(mapper.map(dto));
            }
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors du chargement des parcelles : " + e.getMessage());
            listeParcelles = new ArrayList<>();
        }
    }
    
    /**
     * Charge la liste des cultures pour le sélecteur
     */
    private void chargerListeCultures() {
        try {
            listeCultures = new ArrayList<>();
            List<DtoCulture> dtos = serviceCulture.listerTout();
            for (DtoCulture dto : dtos) {
                listeCultures.add(mapper.map(dto));
            }
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors du chargement des cultures : " + e.getMessage());
            listeCultures = new ArrayList<>();
        }
    }
    
    //------------------
    // Initialisations
    //------------------
    
    public String actualiserCourant() {
        if (courant != null && courant.getIdParcelle() != null && courant.getIdCulture() != null) {
            DtoContenir dto = serviceContenir.retrouver(courant.getIdParcelle(), courant.getIdCulture());
            if (dto == null) {
                UtilJsf.messageError("L'association demandée n'existe pas.");
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
            DtoContenir dto = mapper.map(courant);
            
            // Vérification si c'est une insertion ou une modification
            DtoContenir existant = serviceContenir.retrouver(
                courant.getIdParcelle(), 
                courant.getIdCulture()
            );
            
            if (existant == null) {
                serviceContenir.inserer(dto);
                UtilJsf.messageInfo("Association créée avec succès.");
            } else {
                serviceContenir.modifier(dto);
                UtilJsf.messageInfo("Association modifiée avec succès.");
            }
            
            liste = null;
            return "liste";
        } catch (ExceptionValidation e) {
            UtilJsf.messageError(e.getMessage());
            return null;
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors de l'enregistrement : " + e.getMessage());
            return null;
        }
    }
    
    public String supprimer(Contenir item) {
        try {
            serviceContenir.supprimer(item.getIdParcelle(), item.getIdCulture());
            if (liste != null) {
                liste.remove(item);
            }
            UtilJsf.messageInfo("Suppression effectuée avec succès.");
        } catch (ExceptionValidation e) {
            UtilJsf.messageError(e.getMessage());
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors de la suppression : " + e.getMessage());
        }
        return null;
    }
    
    //-------
    // Recherche / Filtrage
    //-------
    
    /**
     * Filtre par parcelle
     */
    public void filtrerParParcelle() {
        idCultureFiltre = null;
        liste = null;
        getListe();
    }
    
    /**
     * Filtre par culture
     */
    public void filtrerParCulture() {
        idParcelleFiltre = null;
        liste = null;
        getListe();
    }
    
    /**
     * Réinitialise les filtres
     */
    public void reinitialiserFiltres() {
        idParcelleFiltre = null;
        idCultureFiltre = null;
        liste = null;
        getListe();
    }
    
    //-------
    // Méthodes utilitaires
    //-------
    
    /**
     * Récupère la part restante disponible pour une parcelle
     */
    public Double getPartRestante(Integer idParcelle) {
        if (idParcelle == null) {
            return 100.0;
        }
        try {
            return serviceContenir.getPartRestante(idParcelle);
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    /**
     * Récupère le nom d'une parcelle par son ID
     */
    public String getNomParcelle(Integer idParcelle) {
        if (idParcelle == null) {
            return "";
        }
        for (Parcelle p : getListeParcelles()) {
            if (p.getId().equals(idParcelle)) {
                return "Parcelle #" + p.getId() + " (" + p.getSurface() + " m²)";
            }
        }
        return "Parcelle #" + idParcelle;
    }
    
    /**
     * Récupère le nom d'une culture par son ID
     */
    public String getNomCulture(Integer idCulture) {
        if (idCulture == null) {
            return "";
        }
        for (Culture c : getListeCultures()) {
            if (c.getId().equals(idCulture)) {
                return c.getNom();
            }
        }
        return "Culture #" + idCulture;
    }
    
 // === Ajout pour la vue modifierParcelle ===
    private Integer idParcelle; // paramètre passé dans l'URL
    private String nomParcelleSelectionnee;
    private double surfaceParcelleSelectionnee;

    private List<Contenir> listePourParcelle;

    private Double partOccupee = 0.0;
    private Double partRestante = 100.0;

    private Integer idCultureAAjouter;
    private Double partAAjouterPourcent; // saisi en %

    public boolean isParcellePleine() {
        return partRestante <= 0.0001;
    }

    public double getPartOccupeePourcent() { return partOccupee; }
    public double getPartRestantePourcent() { return partRestante; }
    
    public void actualiserPourParcelle() {
        if (idParcelle == null) return;

        try {
            // Charger la parcelle
            DtoParcelle dtoParcelle = serviceParcelle.retrouver(idParcelle);
            Parcelle p = mapper.map(dtoParcelle);
            nomParcelleSelectionnee = "Parcelle #" + p.getId();
            surfaceParcelleSelectionnee = p.getSurface();

            // Charger les associations (cultures liées à cette parcelle)
            List<DtoContenir> dtos = serviceContenir.listerParParcelle(idParcelle);
            listePourParcelle = new ArrayList<>();
            for (DtoContenir dto : dtos) {
                listePourParcelle.add(mapper.map(dto));
            }

            // Calcul de la part occupée et restante
            double somme = 0;
            for (Contenir c : listePourParcelle) {
                somme += c.getPart();
            }
            partOccupee = somme * 100.0;
            partRestante = Math.max(0.0, 100.0 - partOccupee);

        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors du chargement : " + e.getMessage());
            listePourParcelle = new ArrayList<>();
        }
    }

    public String ajouterCulturePourParcelle() {
        try {
            if (idCultureAAjouter == null || partAAjouterPourcent == null) {
                UtilJsf.messageError("Veuillez choisir une culture et saisir une part.");
                return null;
            }

            double partDecimal = partAAjouterPourcent / 100.0;

            if (partDecimal <= 0) {
                UtilJsf.messageError("La part doit être supérieure à 0.");
                return null;
            }

            if (partDecimal * 100.0 > partRestante + 0.001) {
                UtilJsf.messageError("Surface restante insuffisante (" 
                    + String.format("%.2f", partRestante) + "% disponibles).");
                return null;
            }

            DtoContenir dto = new DtoContenir();
            dto.setIdParcelle(idParcelle);
            dto.setIdCulture(idCultureAAjouter);
            dto.setPart(partDecimal);

            serviceContenir.inserer(dto);
            UtilJsf.messageInfo("Culture ajoutée à la parcelle avec succès.");

            // Rafraîchir
            idCultureAAjouter = null;
            partAAjouterPourcent = null;
            actualiserPourParcelle();

        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors de l'ajout : " + e.getMessage());
        }

        return null;
    }

    public String supprimer(int idParcelle, int idCulture) {
        try {
            serviceContenir.supprimer(idParcelle, idCulture);
            UtilJsf.messageInfo("Association supprimée.");
            actualiserPourParcelle();
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors de la suppression : " + e.getMessage());
        }
        return null;
    }
    
    public Integer getIdParcelle() { return idParcelle; }
    public void setIdParcelle(Integer idParcelle) { this.idParcelle = idParcelle; }

    public List<Contenir> getListePourParcelle() { return listePourParcelle; }

    public Integer getIdCultureAAjouter() { return idCultureAAjouter; }
    public void setIdCultureAAjouter(Integer v) { idCultureAAjouter = v; }

    public Double getPartAAjouterPourcent() { return partAAjouterPourcent; }
    public void setPartAAjouterPourcent(Double v) { partAAjouterPourcent = v; }

    public String getNomParcelleSelectionnee() { return nomParcelleSelectionnee; }
    public double getSurfaceParcelleSelectionnee() { return surfaceParcelleSelectionnee; }


}