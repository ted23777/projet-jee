package projet.jsf.model.standard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoCompte;
import projet.commun.dto.DtoMouvement;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceCompte;
import projet.commun.service.IServiceMouvement;
import projet.jsf.data.Compte;
import projet.jsf.data.Mouvement;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelMouvement implements Serializable {

	//-------
	// Champs
	//-------

	private List<Mouvement>		liste;
	private Mouvement			courant;
	private String 				critereRecherche;
	private LocalDate			dateDebut;
	private LocalDate			dateFin;
	private BigDecimal			montantMin;
	private BigDecimal			montantMax;
	private boolean				filtreActif;
	private Integer				compteSelectionne;

	// Pour la sélection du compte
	private List<Compte>		comptesDisponibles;

	@EJB
	private IServiceMouvement	serviceMouvement;

	@EJB
	private IServiceCompte		serviceCompte;

	@Inject
	private IMapper				mapper;

	//-------
	// Getters & Setters
	//-------

	public List<Mouvement> getListe() {
		if (liste == null) {
			chargerListe();
		}
		return liste;
	}

	public Mouvement getCourant() {
		if (courant == null) {
			courant = new Mouvement();
		}
		return courant;
	}

	public void setCourant(Mouvement courant) {
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

	public BigDecimal getMontantMin() {
		return montantMin;
	}

	public void setMontantMin(BigDecimal montantMin) {
		this.montantMin = montantMin;
	}

	public BigDecimal getMontantMax() {
		return montantMax;
	}

	public void setMontantMax(BigDecimal montantMax) {
		this.montantMax = montantMax;
	}

	public boolean isFiltreActif() {
		return filtreActif;
	}

	public void setFiltreActif(boolean filtreActif) {
		this.filtreActif = filtreActif;
	}

	public Integer getCompteSelectionne() {
		return compteSelectionne;
	}

	public void setCompteSelectionne(Integer compteSelectionne) {
		this.compteSelectionne = compteSelectionne;
	}

	public List<Compte> getComptesDisponibles() {
		if (comptesDisponibles == null) {
			chargerComptesDisponibles();
		}
		return comptesDisponibles;
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
			List<DtoMouvement> dtos;

			// Recherche par libellé
			if (critereRecherche != null && !critereRecherche.trim().isEmpty()) {
				dtos = serviceMouvement.rechercherParLibelle(critereRecherche.trim());
			} else {
				dtos = serviceMouvement.listerTout();
			}

			// Filtrage par compte si sélectionné
			if (compteSelectionne != null && compteSelectionne > 0) {
				dtos = dtos.stream()
					.filter(dto -> dto.getIdCompte() == compteSelectionne)
					.collect(Collectors.toList());
			}

			// Filtrage par période et montant
			if (filtreActif) {
				dtos = dtos.stream()
					.filter(dto -> {
						// Filtre de date
						if (dateDebut != null && dto.getDate().isBefore(dateDebut)) {
							return false;
						}
						if (dateFin != null && dto.getDate().isAfter(dateFin)) {
							return false;
						}
						// Filtre de montant
						if (montantMin != null && dto.getMontant().compareTo(montantMin) < 0) {
							return false;
						}
						if (montantMax != null && dto.getMontant().compareTo(montantMax) > 0) {
							return false;
						}
						return true;
					})
					.collect(Collectors.toList());
			}

			for (DtoMouvement dto : dtos) {
				liste.add(mapper.map(dto));
			}
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors du chargement des mouvements : " + e.getMessage());
			liste = new ArrayList<>();
		}
	}

	/**
	 * Charge la liste des comptes disponibles.
	 */
	private void chargerComptesDisponibles() {
		try {
			comptesDisponibles = new ArrayList<>();
			List<DtoCompte> dtos = serviceCompte.listerTout();
			for (DtoCompte dto : dtos) {
				comptesDisponibles.add(mapper.map(dto));
			}
		} catch (Exception e) {
			UtilJsf.messageError("Erreur lors du chargement des comptes : " + e.getMessage());
			comptesDisponibles = new ArrayList<>();
		}
	}

	//------------------
	// Initialisations
	//------------------

	public String actualiserCourant() {
		if (courant != null && courant.getId() != null) {
			DtoMouvement dto = serviceMouvement.retrouver(courant.getId());
			if (dto == null) {
				UtilJsf.messageError("Le mouvement demandé n'existe pas.");
				return "liste";
			} else {
				courant = mapper.map(dto);
			}
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
				UtilJsf.messageError("La date du mouvement est obligatoire.");
				return null;
			}

			// Validation du libellé
			if (courant.getLibelle() == null || courant.getLibelle().trim().isEmpty()) {
				UtilJsf.messageError("Le libellé du mouvement est obligatoire.");
				return null;
			}

			// Validation du montant
			if (courant.getMontant() == null || courant.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
				UtilJsf.messageError("Le montant du mouvement doit être positif.");
				return null;
			}

			// Validation du compte
			if (courant.getIdCompte() == null || courant.getIdCompte() <= 0) {
				UtilJsf.messageError("Le compte associé au mouvement est obligatoire.");
				return null;
			}

			// Insertion ou modification du mouvement
			if (courant.getId() == null) {
				serviceMouvement.inserer(mapper.map(courant));
				UtilJsf.messageInfo("Mouvement créé avec succès !");
			} else {
				serviceMouvement.modifier(mapper.map(courant));
				UtilJsf.messageInfo("Mouvement modifié avec succès !");
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

	public String supprimer(Mouvement item) {
		try {
			serviceMouvement.supprimer(item.getId());
			if (liste != null) {
				liste.remove(item);
			}
			UtilJsf.messageInfo("Mouvement supprimé avec succès !");
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
	 * Lance une recherche par libellé.
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
		montantMin = null;
		montantMax = null;
		compteSelectionne = null;
		filtreActif = false;
		liste = null;
		getListe();
	}

	/**
	 * Active/désactive le filtre par période et montant.
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
	 * Retourne le nombre total de mouvements.
	 */
	public long getNombreTotal() {
		return serviceMouvement.compter();
	}

	/**
	 * Retourne le nombre de mouvements affichés (après filtrage).
	 */
	public int getNombreAffiches() {
		return getListe().size();
	}

	/**
	 * Vérifie si des filtres sont actifs.
	 * IMPORTANT: Cette méthode doit être nommée isFiltresActifs() pour respecter la convention JavaBeans
	 * et permettre l'utilisation dans les expressions EL JSF comme #{modelMouvement.filtresActifs}
	 */
	public boolean isFiltresActifs() {
		return (critereRecherche != null && !critereRecherche.trim().isEmpty())
			|| (compteSelectionne != null && compteSelectionne > 0)
			|| (filtreActif && (dateDebut != null || dateFin != null || montantMin != null || montantMax != null));
	}

	/**
	 * Retourne le nom du compte associé à un mouvement pour l'affichage.
	 */
	public String getNomCompte(Mouvement mouvement) {
		try {
			DtoCompte compte = serviceCompte.retrouver(mouvement.getIdCompte());
			if (compte != null) {
				return compte.getNom() + " " + (compte.getPrenom() != null ? compte.getPrenom() : "");
			}
			return "Compte inconnu";
		} catch (Exception e) {
			return "Erreur";
		}
	}

	/**
	 * Calcule le total des mouvements affichés.
	 */
	public BigDecimal getTotalAffiches() {
		return getListe().stream()
			.map(Mouvement::getMontant)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
