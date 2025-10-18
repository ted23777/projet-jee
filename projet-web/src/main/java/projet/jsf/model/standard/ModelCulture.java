package projet.jsf.model.standard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoCulture;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceCulture;   // <-- même package que pour Compte (commun.service)
import projet.jsf.data.Culture;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelCulture implements Serializable {

	//-------
	// Champs
	//-------
	
	private List<Culture>	liste;
	private Culture			courant;
	private String 			critereRecherche;

	@EJB
	private IServiceCulture	serviceCulture;

	@Inject
	private IMapper			mapper;

	//-------
	// Getters
	//-------

	public List<Culture> getListe() {
		if (liste == null) {
			chargerListe();
		}
		return liste;
	}

	public Culture getCourant() {
		if (courant == null) {
			courant = new Culture();
		}
		return courant;
	}
	
	public void setCourant(Culture courant) {
        this.courant = courant;
    }
	
	public String getCritereRecherche() {
        return critereRecherche;
    }

    public void setCritereRecherche(String critereRecherche) {
        this.critereRecherche = critereRecherche;
    }

	//------------------
	// Méthodes internes
	//------------------

	/**
	 * Charge la liste selon le critère de recherche s'il est renseigné,
	 * sinon charge toute la liste.
	 */
	private void chargerListe() {
		try {
			liste = new ArrayList<>();
			List<DtoCulture> dtos;

			if (critereRecherche != null && !critereRecherche.trim().isEmpty()) {
				dtos = serviceCulture.rechercherParNom(critereRecherche.trim());
			} else {
				dtos = serviceCulture.listerTout();
			}
			for (DtoCulture dto : dtos) {
				liste.add(mapper.map(dto));
			}
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors du chargement des cultures : " + e.getMessage());
			liste = new ArrayList<>();
		}
	}

	//------------------
	// Initialisations
	//------------------

	public String actualiserCourant() {
		if (courant != null && courant.getId() != null) {
			DtoCulture dto = serviceCulture.retrouver(courant.getId());
			if (dto == null) {
				UtilJsf.messageError("La culture demandée n'existe pas.");
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
			if (courant.getId() == null) {
				serviceCulture.inserer(mapper.map(courant));
			} else {
				serviceCulture.modifier(mapper.map(courant));
			}
			UtilJsf.messageInfo("Mise à jour effectuée avec succès.");
			// Invalide le cache local pour refléter les changements au prochain affichage
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

	public String supprimer(Culture item) {
		try {
			serviceCulture.supprimer(item.getId());
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

	/** Lance une recherche par nom (utilise critereRecherche). */
	public void rechercher() {
		liste = null;   // force le rechargement via getListe()
		getListe();
	}

	/** Réinitialise le critère et recharge la liste complète. */
	public void reinitialiserRecherche() {
		critereRecherche = null;
		liste = null;
		getListe();
	}
}
