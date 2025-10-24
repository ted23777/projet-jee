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
import projet.commun.service.IServiceParcelle;
import projet.jsf.data.Contenir;
import projet.jsf.data.Culture;
import projet.jsf.data.Parcelle;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.CompteActif;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelContenir implements Serializable {

	private List<Contenir> liste;
	private Contenir courant;
	// private List<Culture> culturesDisponibles;
	private List<Parcelle> parcellesDisponibles;

	// -------
	// Champs pour parcelleculture.xhtml
	// -------
	private Integer idParcelleSelectionnee;
	private Integer idCultureAAjouter;
	private Double partAAjouter;

	private String nomCultureAAjouter;

	
	
	@EJB
	private IServiceContenir serviceContenir;

	@EJB
	private IServiceParcelle serviceParcelle;

	@Inject
	private ModelCulture modelCulture;

	@Inject
	private ModelParcelle modelParcelle;

	@Inject
	private IMapper mapper;

	@Inject
	private CompteActif compte;

	// -------
	// Getters
	// -------
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

	// -------
	// Getters/Setters
	// -------
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

	
	// Getter/Setter
		public String getNomCultureAAjouter() {
		    return nomCultureAAjouter;
		}

		public void setNomCultureAAjouter(String nomCultureAAjouter) {
		    this.nomCultureAAjouter = nomCultureAAjouter;
		}
	// -------
	// Initialisations
	// -------
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

	// -------
	// Actions
	// -------
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

	// -------
	// Use cases MVP
	// -------
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

	// ERREUR AU NIVEAU DE L'INTERFACE DE IServiceContenir
	public Double getPartRestante(int idParcelle) {
		return serviceContenir.getPartRestante(idParcelle);
	}

	// -------
	// Méthodes pour les vues
	// -------

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
		if (idParcelle == null)
			return 0.0;
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
	public void ajouterCulture(DtoContenir dto) {
		try {
			// Vérification de la part et validation avant d'ajouter
			if (dto.getPart() <= 0) {
				throw new ExceptionValidation("La part doit être positive.");
			}

			// Récupérer la somme des parts de la parcelle et vérifier si elle dépasse 100%
			double sommeParts = getSommePartsParcelle(dto.getIdParcelle());
			if (sommeParts + dto.getPart() > 100) {
				throw new ExceptionValidation("La somme des parts dépasse 100 %.");
			}

			// Ajout de la culture à la parcelle
			serviceContenir.ajouterCultureAParcelle(dto);
			UtilJsf.messageInfo("Culture ajoutée à la parcelle !");
			liste = null; // Réinitialiser la liste pour recharger les données
		} catch (ExceptionValidation e) {
			UtilJsf.messageError(e);
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

	public List<Integer> getParcellesDeCulture(int idCulture) {
		List<Integer> ids = new ArrayList<>();
		for (DtoContenir c : serviceContenir.listerParCulture(idCulture)) {
			ids.add(c.getIdParcelle());
		}
		return ids;
	}

	public ModelParcelle getModelParcelle() {
		return modelParcelle;
	}

	public void setModelParcelle(ModelParcelle modelParcelle) {
		this.modelParcelle = modelParcelle;
	}

	public ModelCulture getModelCulture() {
		return modelCulture;
	}

	public void setModelCulture(ModelCulture modelCulture) {
		this.modelCulture = modelCulture;
	}

	public String getNomCulture(Integer idCulture) {
		if (idCulture == null) {
			return "";
		}

		// Chercher dans la liste des cultures
		for (Culture c : modelCulture.getListe()) {
			if (c.getId().equals(idCulture)) {
				return c.getNom();
			}
		}

		// Si pas trouvé, interroger le service
		try {
			DtoCulture dto = modelCulture.getServiceCulture().retrouver(idCulture);
			if (dto != null) {
				Culture culture = mapper.map(dto);
				return culture.getNom();
			}
		} catch (Exception e) {
			// Ignorer
		}

		return "Culture #" + idCulture;
	}

	public List<Contenir> listerParParcelle(Integer idParcelle) {
		if (idParcelle == null) {
			return new ArrayList<>();
		}

		List<Contenir> resultat = new ArrayList<>();
		try {
			List<DtoContenir> dtos = serviceContenir.listerParParcelle(idParcelle);
			for (DtoContenir dto : dtos) {
				resultat.add(mapper.map(dto));
			}
		} catch (Exception e) {
			UtilJsf.messageError("Erreur : " + e.getMessage());
		}
		return resultat;
	}

	public void ajouterCulturePourParcelle() {
	    if (idParcelleSelectionnee == null) {
	        UtilJsf.messageError("Veuillez sélectionner une parcelle.");
	        return;
	    }
	    
	    if (idCultureAAjouter == null) {
	        UtilJsf.messageError("Veuillez sélectionner une culture.");
	        return;
	    }
	    
	    if (partAAjouter == null || partAAjouter <= 0) {
	        UtilJsf.messageError("La part doit être supérieure à 0.");
	        return;
	    }
	    
	    try {
	        serviceContenir.ajouterCultureAParcelle(
	            idParcelleSelectionnee, 
	            idCultureAAjouter, 
	            partAAjouter
	        );
	        UtilJsf.messageInfo("Culture ajoutée avec succès !");
	        
	        // Réinitialiser
	        idCultureAAjouter = null;
	        partAAjouter = null;
	        
	    } catch (ExceptionValidation e) {
	        UtilJsf.messageError(e);
	    }
	}
	
}
