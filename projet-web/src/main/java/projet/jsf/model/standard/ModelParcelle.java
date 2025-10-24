package projet.jsf.model.standard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoCompte;
import projet.commun.dto.DtoCulture;
import projet.commun.dto.DtoParcelle;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceCompte;
import projet.commun.service.IServiceConnexion;
import projet.commun.service.IServiceContenir;
import projet.commun.service.IServiceParcelle;
import projet.jsf.data.Contenir;
import projet.jsf.data.Parcelle;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.CompteActif;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelParcelle implements Serializable {

	// -------
	// Champs
	// -------
	private List<Parcelle> liste;

	private Parcelle courant;

	@EJB
	private IServiceParcelle serviceParcelle;

	@EJB
	private IServiceCompte serviceCompte; // Ajouter cette injection

	@EJB
	private IServiceContenir serviceContenir;

	@Inject
	private IMapper mapper;

	@Inject
	private CompteActif compte;

	// -------
	// Getters
	// -------
	public List<Parcelle> getListe() {
		if (liste == null) {
			liste = new ArrayList<>();
			for (DtoParcelle dto : serviceParcelle.listerTout()) {
				liste.add(mapper.map(dto));
			}
		}
		return liste;
	}

	public Parcelle getCourant() {
		if (courant == null) {
			courant = new Parcelle();
		}
		return courant;
	}

	// -------
	// Initialisations
	// -------
	public String actualiserCourant() {
		if (courant != null && courant.getId() != null) {
			DtoParcelle dto = serviceParcelle.retrouver(courant.getId());
			if (dto == null) {
				UtilJsf.messageError("La parcelle demandée n'existe pas.");
				return "liste";
			} else {
				courant = mapper.map(dto);
			}
		}
		return null;
	}

	// -------
	// Actions principales
	// -------
	public String validerMiseAJour() {
		try {
			  // Si la parcelle est marquée comme libre, on retire le propriétaire
	        if (courant.getLibre()) {
	            courant.setIdCompte(null); // Enlève le propriétaire

	        } else {
	            // Si la parcelle est occupée, on laisse le propriétaire
	            if (courant.getIdCompte() == null) {
	                throw new ExceptionValidation("Un propriétaire doit être attribué si la parcelle n'est pas libre.");
	            }
	        }
			if (courant.getId() == null) {
				serviceParcelle.inserer(mapper.map(courant));
			} else {
				serviceParcelle.modifier(mapper.map(courant));
			}
			UtilJsf.messageInfo("Mise à jour effectuée avec succès.");
			liste = null;
			return "liste";
		} catch (ExceptionValidation e) {
			UtilJsf.messageError(e);
			return null;
		}
	}

	public String supprimer(Parcelle item) {
		try {
			serviceParcelle.supprimer(item.getId());
			getListe().remove(item);
			UtilJsf.messageInfo("Suppression effectuée avec succès.");
		} catch (ExceptionValidation e) {
			UtilJsf.messageError(e);
		}
		return null;
	}

	// -------
	// Actions spécifiques MVP
	// -------

	public List<Parcelle> getParcellesLibres() {
		List<Parcelle> parcellesLibres = new ArrayList<>();
		for (DtoParcelle dto : serviceParcelle.listerLibres()) {
			parcellesLibres.add(mapper.map(dto));
		}
		return parcellesLibres;
	}

	public List<Parcelle> getParcellesOccupees() {
		List<Parcelle> parcellesOccupees = new ArrayList<>();
		for (DtoParcelle dto : serviceParcelle.listerOccupees()) {
			parcellesOccupees.add(mapper.map(dto));
		}
		return parcellesOccupees;
	}

	public boolean estLibre(Parcelle p) {
		return serviceParcelle.estLibre(p.getId());
	}

	public String libererParcelle(Parcelle p) {
		serviceParcelle.libererParcelle(p.getId());
		UtilJsf.messageInfo("Parcelle libérée avec succès.");
		liste = null;
		return null;
	}

	public String occuperParcelle(Parcelle p) {
		serviceParcelle.occuperParcelle(p.getId());
		UtilJsf.messageInfo("Parcelle marquée comme occupée.");
		liste = null;
		return null;
	}

	// -------
	// Méthodes pour les vues
	// -------

	/**
	 * Récupère toutes les associations contenir
	 */
	public List<Contenir> getListeContenir() {
		// Appel au service pour récupérer toutes les associations
		List<Contenir> contenirs = new ArrayList<>();
		// TODO: injecter IServiceContenir et appeler listerTout()
		return contenirs;
	}

	/**
	 * Récupère le nom d'une parcelle
	 */
	public String getNomParcelle(Integer idParcelle) {
		// 1. Vérification de l'argument (conservée car bonne pratique)
		if (idParcelle == null) {
			return "";
		}

		// 2. Recherche classique avec une boucle for
		Parcelle parcelleTrouvee = null;
		for (Parcelle p : liste) {
			// La méthode equals est utilisée pour comparer deux Integer
			if (p.getId().equals(idParcelle)) {
				parcelleTrouvee = p;
				break; // Sortir de la boucle dès que l'élément est trouvé
			}
		}

		// 3. Construction de la chaîne de caractères
		if (parcelleTrouvee != null) {
			// Cas où la parcelle a été trouvée
			return "Parcelle #" + parcelleTrouvee.getId() + " (" + parcelleTrouvee.getSurface() + " m²)";
		} else {
			// Cas où la parcelle n'a pas été trouvée (le "orElse" original)
			return "Parcelle #" + idParcelle;
		}
	}

	/**
	 * Récupère le nom d'une culture
	 */
	public String getNomCulture(Integer idCulture) {
		// TODO: injecter ModelCulture ou IServiceCulture
		return "Culture #" + idCulture;
	}


	/**
	 * Supprime une culture d'une parcelle
	 */
	public String supprimerCulture(Integer idParcelle, Integer idCulture) {
		try {
			// TODO: appeler le service pour supprimer l'association
			UtilJsf.messageInfo("Culture supprimée avec succès.");
			return null;
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors de la suppression : " + e.getMessage());
			return null;
		}
	}

	/**
	 * Récupère le nom du propriétaire de la parcelle
	 */
	public String getNomProprietaire(Integer idCompte) {
		if (idCompte == null) {
			return "Non attribuée";
		}
		try {
			DtoCompte compte = serviceCompte.retrouver(idCompte);
			if (compte != null) {
				return compte.getNom() + " " + compte.getPrenom();
			}
		} catch (Exception e) {
			// Ignorer
		}
		return "Compte #" + idCompte;
	}

	/**
	 * Calcule la surface restante en m²
	 *//// AVOIR
	public double getSurfaceRestante(Parcelle parcelle) {
	    if (parcelle == null || parcelle.getId() == null) {
	        return 0.0;
	    }
	    
	    try {
	        Double partRestante = serviceContenir.getPartRestante(parcelle.getId());
	        if (partRestante == null) {
	            partRestante = 100.0;
	        }
	        return (parcelle.getSurface() * partRestante) / 100.0;
	    } catch (Exception e) {
	        return 0.0;
	    }
	}

	/**
	 * Récupère les parcelles du compte connecté
	 */
	public List<Parcelle> getParcellesDuCompte() {
			List<Parcelle> parcellesAssociees = new ArrayList<>();
			for (DtoParcelle dto : serviceParcelle.listerParCompte(compte.getId())) {
				parcellesAssociees.add(mapper.map(dto));
			}
		return parcellesAssociees;
	}

	/**
  * Réserve une parcelle pour l'utilisateur connecté
  */
 public String reserverParcelle(Parcelle parcelle) {
		
	 try {
	        // Modifier les valeurs AVANT
	        parcelle.setIdCompte(compte.getId());
	        parcelle.setLibre(false);
	        
	        // Sauvegarder
	        serviceParcelle.modifier(mapper.map(parcelle));
	        
	        UtilJsf.messageInfo("Parcelle réservée avec succès !");
	        liste = null;
	        return "liste";
	    } catch (ExceptionValidation e) {
	        UtilJsf.messageError(e);
	        return null;
	    }
	 }
}
