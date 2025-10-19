package projet.jsf.model.standard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.*;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.*;
import projet.jsf.data.*;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelParcelle implements Serializable {

	// ========================================
	// === PARTIE PARCELLE (ancienne classe) ===
	// ========================================

	private List<Parcelle> liste;
	private Parcelle courant;
	private List<DtoCompte> comptes; // pour la combo

	@EJB
	private IServiceParcelle serviceParcelle;
	@EJB
	private IServiceCompte serviceCompte;

	// ========================================
	// === PARTIE CONTENIR (fusionnée) ===
	// ========================================

	private List<Contenir> listeContenir;
	private Contenir courantContenir;
	private Integer idParcelleFiltre;
	private Integer idCultureFiltre;

	private List<Parcelle> listeParcelles;
	private List<Culture> listeCultures;

	@EJB
	private IServiceContenir serviceContenir;
	@EJB
	private IServiceCulture serviceCulture;

	@Inject
	private IMapper mapper;

	// === Pour la gestion des cultures d'une parcelle ===
	private Integer idParcelle;
	private String nomParcelleSelectionnee;
	private double surfaceParcelleSelectionnee;
	private List<Contenir> listePourParcelle;

	private Double partOccupee = 0.0;
	private Double partRestante = 100.0;

	private Integer idCultureAAjouter;
	private Double partAAjouterPourcent;

	// ==================================================
	// === MÉTHODES ISSUES DE ModelParcelle ORIGINEL ===
	// ==================================================

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
			courant.setLibre(true);
		}
		return courant;
	}

	public List<DtoCompte> getComptes() {
		if (comptes == null) {
			comptes = serviceCompte.listerTout();
		}
		return comptes;
	}

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

	public String validerMiseAJour() {
		try {
			if (courant.getSurface() <= 0) {
				UtilJsf.messageError("La surface doit être > 0.");
				return null;
			}
			if (courant.getId() == null) {
				serviceParcelle.inserer(mapper.map(courant));
			} else {
				serviceParcelle.modifier(mapper.map(courant));
			}
			UtilJsf.messageInfo("Mise à jour effectuée avec succès.");
			liste = null;
			return "liste";
		} catch (Exception e) {
			UtilJsf.messageError(e);
			return null;
		}
	}

	public String supprimer(Parcelle item) {
		try {
			serviceParcelle.supprimer(item.getId());
			if (liste != null) {
				liste.remove(item);
			}
			UtilJsf.messageInfo("Suppression effectuée avec succès.");
		} catch (Exception e) {
			UtilJsf.messageError(e);
		}
		return null;
	}

	// ==================================================
	// === MÉTHODES ISSUES DE ModelContenir (fusion) ===
	// ==================================================

	public List<Contenir> getListeContenir() {
		if (listeContenir == null) {
			chargerListeContenir();
		}
		return listeContenir;
	}

	private void chargerListeContenir() {
		try {
			listeContenir = new ArrayList<>();
			List<DtoContenir> dtos;
			if (idParcelleFiltre != null) {
				dtos = serviceContenir.listerParParcelle(idParcelleFiltre);
			//} else if (idCultureFiltre != null) {
			//	dtos = serviceContenir.listerParCulture(idCultureFiltre);
			} else {
				dtos = serviceContenir.listerTout();
			}
			for (DtoContenir dto : dtos) {
				listeContenir.add(mapper.map(dto));
			}
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors du chargement des associations : " + e.getMessage());
			listeContenir = new ArrayList<>();
		}
	}

	public Contenir getCourantContenir() {
		if (courantContenir == null)
			courantContenir = new Contenir();
		return courantContenir;
	}

	public String validerMiseAJourContenir() {
		try {
			DtoContenir dto = mapper.map(courantContenir);
			DtoContenir existant = serviceContenir.retrouver(courantContenir.getIdParcelle(),
					courantContenir.getIdCulture());
			if (existant == null) {
				serviceContenir.inserer(dto);
				UtilJsf.messageInfo("Association créée avec succès.");
			} else {
				serviceContenir.modifier(dto);
				UtilJsf.messageInfo("Association modifiée avec succès.");
			}
			listeContenir = null;
			return "liste";
		} catch (ExceptionValidation e) {
			UtilJsf.messageError(e.getMessage());
			return null;
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors de l'enregistrement : " + e.getMessage());
			return null;
		}
	}

	public String supprimerContenir(Contenir item) {
		try {
			serviceContenir.supprimer(item.getIdParcelle(), item.getIdCulture());
			if (listeContenir != null)
				listeContenir.remove(item);
			UtilJsf.messageInfo("Suppression de l'association réussie.");
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors de la suppression : " + e.getMessage());
		}
		return null;
	}

	// === Filtres et chargement ===

	public void filtrerParParcelle() {
		idCultureFiltre = null;
		listeContenir = null;
		getListeContenir();
	}

	public void filtrerParCulture() {
		idParcelleFiltre = null;
		listeContenir = null;
		getListeContenir();
	}

	public void reinitialiserFiltres() {
		idParcelleFiltre = null;
		idCultureFiltre = null;
		listeContenir = null;
		getListeContenir();
	}

	// === Fonctions pour l'affichage ===

	public String getNomParcelle(Integer idParcelle) {
		if (idParcelle == null)
			return "";
		for (Parcelle p : getListeParcelles()) {
			if (p.getId().equals(idParcelle)) {
				return "Parcelle #" + p.getId() + " (" + p.getSurface() + " m²)";
			}
		}
		return "Parcelle #" + idParcelle;
	}

	public String getNomCulture(Integer idCulture) {
		if (idCulture == null)
			return "";
		for (Culture c : getListeCultures()) {
			if (c.getId().equals(idCulture))
				return c.getNom();
		}
		return "Culture #" + idCulture;
	}

	// === Gestion spécifique à une parcelle ===

	public void actualiserPourParcelle() {
		if (idParcelle == null)
			return;
		try {
			DtoParcelle dtoParcelle = serviceParcelle.retrouver(idParcelle);
			Parcelle p = mapper.map(dtoParcelle);
			nomParcelleSelectionnee = "Parcelle #" + p.getId();
			surfaceParcelleSelectionnee = p.getSurface();

			List<DtoContenir> dtos = serviceContenir.listerParParcelle(idParcelle);
			listePourParcelle = new ArrayList<>();
			for (DtoContenir dto : dtos) {
				listePourParcelle.add(mapper.map(dto));
			}

			double somme = 0;
			for (Contenir c : listePourParcelle)
				somme += c.getPart();
			partOccupee = somme * 100.0;
			partRestante = Math.max(0.0, 100.0 - partOccupee);

		} catch (Exception e) {
			UtilJsf.messageError("Erreur de chargement : " + e.getMessage());
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
				UtilJsf.messageError(
						"Surface restante insuffisante (" + String.format("%.2f", partRestante) + "% restantes).");
				return null;
			}

			DtoContenir dto = new DtoContenir();
			dto.setIdParcelle(idParcelle);
			dto.setIdCulture(idCultureAAjouter);
			dto.setPart(partDecimal);

			serviceContenir.inserer(dto);
			UtilJsf.messageInfo("Culture ajoutée à la parcelle avec succès.");
			idCultureAAjouter = null;
			partAAjouterPourcent = null;
			actualiserPourParcelle();

		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors de l'ajout : " + e.getMessage());
		}
		return null;
	}

	public String supprimerCulture(int idParcelle, int idCulture) {
		try {
			serviceContenir.supprimer(idParcelle, idCulture);
			UtilJsf.messageInfo("Association supprimée.");
			actualiserPourParcelle();
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors de la suppression : " + e.getMessage());
		}
		return null;
	}

	// === Utilitaires ===

	public boolean isParcellePleine() {
		return partRestante <= 0.0001;
	}

	public double getPartOccupeePourcent() {
		return partOccupee;
	}

	public double getPartRestantePourcent() {
		return partRestante;
	}

	public List<Parcelle> getListeParcelles() {
		if (listeParcelles == null) {
			listeParcelles = new ArrayList<>();
			for (DtoParcelle dto : serviceParcelle.listerTout()) {
				listeParcelles.add(mapper.map(dto));
			}
		}
		return listeParcelles;
	}

	public List<Culture> getListeCultures() {
		if (listeCultures == null) {
			listeCultures = new ArrayList<>();
			for (DtoCulture dto : serviceCulture.listerTout()) {
				listeCultures.add(mapper.map(dto));
			}
		}
		return listeCultures;
	}

	// === Getters / Setters ===

	public Integer getIdParcelle() {
		return idParcelle;
	}

	public void setIdParcelle(Integer idParcelle) {
		this.idParcelle = idParcelle;
	}

	public List<Contenir> getListePourParcelle() {
		return listePourParcelle;
	}

	public Integer getIdCultureAAjouter() {
		return idCultureAAjouter;
	}

	public void setIdCultureAAjouter(Integer v) {
		idCultureAAjouter = v;
	}

	public Double getPartAAjouterPourcent() {
		return partAAjouterPourcent;
	}

	public void setPartAAjouterPourcent(Double v) {
		partAAjouterPourcent = v;
	}

	public String getNomParcelleSelectionnee() {
		return nomParcelleSelectionnee;
	}

	public double getSurfaceParcelleSelectionnee() {
		return surfaceParcelleSelectionnee;
	}
}
