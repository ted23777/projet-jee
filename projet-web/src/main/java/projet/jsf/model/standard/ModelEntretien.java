package projet.jsf.model.standard;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoConcerner;
import projet.commun.dto.DtoCulture;
import projet.commun.dto.DtoEntretien;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceConcerner;
import projet.commun.service.IServiceCulture;
import projet.commun.service.IServiceEntretien;
import projet.jsf.data.Culture;
import projet.jsf.data.Entretien;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelEntretien implements Serializable {

	//-------
	// Champs
	//-------

	private List<Entretien>		liste;
	private Entretien			courant;
	private String 				critereRecherche;
	private LocalDate			dateDebut;
	private LocalDate			dateFin;
	private boolean				filtreActif;

	// Pour gérer les cultures concernées par l'entretien
	private List<Culture>		culturesDisponibles;
	private List<Integer>		culturesConcerneesIds;

	@EJB
	private IServiceEntretien	serviceEntretien;

	@EJB
	private IServiceCulture		serviceCulture;

	@EJB
	private IServiceConcerner	serviceConcerner;

	@Inject
	private IMapper				mapper;

	//-------
	// Getters & Setters
	//-------

	public List<Entretien> getListe() {
		if (liste == null) {
			chargerListe();
		}
		return liste;
	}

	public Entretien getCourant() {
		if (courant == null) {
			courant = new Entretien();
		}
		return courant;
	}

	public void setCourant(Entretien courant) {
        this.courant = courant;
    }

	public String getCritereRecherche() {
        return critereRecherche;
    }

    public void setCritereRecherche(String critereRecherche) {
        this.critereRecherche = critereRecherche;
    }

    public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}

	public boolean isFiltreActif() {
		return filtreActif;
	}

	public void setFiltreActif(boolean filtreActif) {
		this.filtreActif = filtreActif;
	}

	public List<Culture> getCulturesDisponibles() {
		if (culturesDisponibles == null) {
			chargerCulturesDisponibles();
		}
		return culturesDisponibles;
	}

	public List<Integer> getCulturesConcerneesIds() {
		return culturesConcerneesIds;
	}

	public void setCulturesConcerneesIds(List<Integer> culturesConcerneesIds) {
		this.culturesConcerneesIds = culturesConcerneesIds;
	}

	//------------------
	// Méthodes internes
	//------------------

	/**
	 * Charge la liste selon les critères de recherche et de filtrage.
	 */
	private void chargerListe() {
		try {
			liste = new ArrayList<>();
			List<DtoEntretien> dtos;

			// Recherche par titre
			if (critereRecherche != null && !critereRecherche.trim().isEmpty()) {
				dtos = serviceEntretien.rechercherParTitre(critereRecherche.trim());
			} else {
				dtos = serviceEntretien.listerTout();
			}

			// Filtrage par période
			if (filtreActif && (dateDebut != null || dateFin != null)) {
				dtos = dtos.stream()
					.filter(dto -> {
						if (dateDebut != null && dto.getDate().isBefore(dateDebut)) {
							return false;
						}
						if (dateFin != null && dto.getDate().isAfter(dateFin)) {
							return false;
						}
						return true;
					})
					.collect(Collectors.toList());
			}

			for (DtoEntretien dto : dtos) {
				liste.add(mapper.map(dto));
			}
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors du chargement des entretiens : " + e.getMessage());
			liste = new ArrayList<>();
		}
	}

	/**
	 * Charge la liste des cultures disponibles pour l'association.
	 */
	private void chargerCulturesDisponibles() {
		try {
			culturesDisponibles = new ArrayList<>();
			List<DtoCulture> dtos = serviceCulture.listerTout();
			for (DtoCulture dto : dtos) {
				culturesDisponibles.add(mapper.map(dto));
			}
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors du chargement des cultures : " + e.getMessage());
			culturesDisponibles = new ArrayList<>();
		}
	}

	/**
	 * Charge les cultures concernées par l'entretien courant.
	 */
	private void chargerCulturesConcernees() {
		if (courant != null && courant.getId() != null) {
			try {
				List<DtoConcerner> concerners = serviceConcerner.listerParIdEntretien(courant.getId());
				culturesConcerneesIds = concerners.stream()
					.map(DtoConcerner::getIdCulture)
					.collect(Collectors.toList());
			} catch (Exception e) {
				UtilJsf.messageError("Erreur lors du chargement des cultures concernées : " + e.getMessage());
				culturesConcerneesIds = new ArrayList<>();
			}
		} else {
			culturesConcerneesIds = new ArrayList<>();
		}
	}

	//------------------
	// Initialisations
	//------------------

	public String actualiserCourant() {
		if (courant != null && courant.getId() != null) {
			DtoEntretien dto = serviceEntretien.retrouver(courant.getId());
			if (dto == null) {
				UtilJsf.messageError("L'entretien demandé n'existe pas.");
				return "liste";
			} else {
				courant = mapper.map(dto);
				chargerCulturesConcernees();
			}
		} else {
			culturesConcerneesIds = new ArrayList<>();
		}
		return null;
	}

	//-------
	// Actions CRUD
	//-------

	public String validerMiseAJour() {
		try {
			// Validation de la date
			if (courant.getDate() == null) {
				UtilJsf.messageError("La date de l'entretien est obligatoire.");
				return null;
			}

			// Validation du titre
			if (courant.getTitre() == null || courant.getTitre().trim().isEmpty()) {
				UtilJsf.messageError("Le titre de l'entretien est obligatoire.");
				return null;
			}

			int idEntretien;

			// Insertion ou modification de l'entretien
			if (courant.getId() == null) {
				idEntretien = serviceEntretien.inserer(mapper.map(courant));
				courant.setId(idEntretien);
				UtilJsf.messageInfo("Entretien créé avec succès !");
			} else {
				serviceEntretien.modifier(mapper.map(courant));
				idEntretien = courant.getId();
				UtilJsf.messageInfo("Entretien modifié avec succès !");
			}

			// Gestion des cultures concernées
			if (culturesConcerneesIds != null) {
				gererCulturesConcernees(idEntretien);
			}

			// Invalide le cache local pour refléter les changements
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

	/**
	 * Gère l'ajout et la suppression des associations culture-entretien.
	 */
	private void gererCulturesConcernees(int idEntretien) throws ExceptionValidation {
		// Récupérer les associations existantes
		List<DtoConcerner> existants = serviceConcerner.listerParIdEntretien(idEntretien);
		List<Integer> idsExistants = existants.stream()
			.map(DtoConcerner::getIdCulture)
			.collect(Collectors.toList());

		// Supprimer les associations qui ne sont plus sélectionnées
		for (Integer idExistant : idsExistants) {
			if (!culturesConcerneesIds.contains(idExistant)) {
				serviceConcerner.supprimer(idExistant, idEntretien);
			}
		}

		// Ajouter les nouvelles associations
		for (Integer idCulture : culturesConcerneesIds) {
			if (!idsExistants.contains(idCulture)) {
				DtoConcerner nouveau = new DtoConcerner();
				nouveau.setIdCulture(idCulture);
				nouveau.setIdEntretien(idEntretien);
				serviceConcerner.inserer(nouveau);
			}
		}
	}

	public String supprimer(Entretien item) {
		try {
			// Supprimer d'abord toutes les associations
			List<DtoConcerner> concerners = serviceConcerner.listerParIdEntretien(item.getId());
			for (DtoConcerner concerner : concerners) {
				serviceConcerner.supprimer(concerner.getIdCulture(), concerner.getIdEntretien());
			}

			// Puis supprimer l'entretien
			serviceEntretien.supprimer(item.getId());

			if (liste != null) {
				liste.remove(item);
			}
			UtilJsf.messageInfo("Entretien supprimé avec succès !");
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
	 * Lance une recherche par titre.
	 */
	public void rechercher() {
		liste = null;
		getListe();
	}

	/**
	 * Réinitialise tous les critères de recherche et de filtrage.
	 */
	public void reinitialiserRecherche() {
		critereRecherche = null;
		dateDebut = null;
		dateFin = null;
		filtreActif = false;
		liste = null;
		getListe();
	}

	/**
	 * Active/désactive le filtre par période.
	 */
	public void toggleFiltre() {
		filtreActif = !filtreActif;
		liste = null;
		getListe();
	}

	//-------
	// Méthodes utilitaires
	//-------

	/**
	 * Retourne le nombre total d'entretiens.
	 */
	public long getNombreTotal() {
		return serviceEntretien.compter();
	}

	/**
	 * Retourne le nombre d'entretiens affichés (après filtrage).
	 */
	public int getNombreAffiches() {
		return getListe().size();
	}

	/**
	 * Vérifie si des filtres sont actifs.
	 */
	public boolean hasFiltresActifs() {
		return (critereRecherche != null && !critereRecherche.trim().isEmpty())
			|| (filtreActif && (dateDebut != null || dateFin != null));
	}

	/**
	 * Retourne les cultures concernées par un entretien pour l'affichage.
	 */
	public String getCulturesNoms(Entretien entretien) {
		try {
			List<DtoConcerner> concerners = serviceConcerner.listerParIdEntretien(entretien.getId());
			if (concerners.isEmpty()) {
				return "Aucune culture";
			}

			List<String> noms = new ArrayList<>();
			for (DtoConcerner concerner : concerners) {
				DtoCulture culture = serviceCulture.retrouver(concerner.getIdCulture());
				if (culture != null) {
					noms.add(culture.getNom());
				}
			}

			return noms.isEmpty() ? "Aucune culture" : String.join(", ", noms);
		} catch (Exception e) {
			return "Erreur";
		}
	}
}
