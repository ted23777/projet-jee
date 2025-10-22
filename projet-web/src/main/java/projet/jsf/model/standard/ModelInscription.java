package projet.jsf.model.standard;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoCompte;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceCompte;
import projet.jsf.data.Compte;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@RequestScoped
@Named
public class ModelInscription {

	// -------
	// Champs
	// -------

	private Compte courant;
	private String confirmMotDePasse;

	@EJB
	private IServiceCompte serviceCompte;
	@Inject
	private IMapper mapper;
	@Inject
	private ModelInfo modelInfo;

	// -------
	// Getters & Setters
	// -------

	public Compte getCourant() {
		if (courant == null) {
			courant = new Compte();
		}
		return courant;
	}

	public void setCourant(Compte courant) {
		this.courant = courant;
	}

	public String getConfirmMotDePasse() {
		return confirmMotDePasse;
	}

	public void setConfirmMotDePasse(String confirmMotDePasse) {
		this.confirmMotDePasse = confirmMotDePasse;
	}

	// -------
	// Actions
	// -------

	public String inscrire() {
		try {
			// Validation du mot de passe
			if (courant.getMotDePasse() == null || courant.getMotDePasse().length() < 6) {
				UtilJsf.messageError("Le mot de passe doit contenir au moins 6 caract�res.");
				return null;
			}

			// Validation de la confirmation du mot de passe
			if (!courant.getMotDePasse().equals(confirmMotDePasse)) {
				UtilJsf.messageError("Les mots de passe ne correspondent pas.");
				return null;
			}

			// Validation de l'email
			if (courant.getAdresseMail() == null || !courant.getAdresseMail().contains("@")) {
				UtilJsf.messageError("L'adresse mail n'est pas valide.");
				return null;
			}

			// Pr�paration du DTO
			DtoCompte dto = mapper.map(courant);
			dto.setAdmin(false); // Les nouveaux comptes ne sont pas administrateurs
			dto.setSolde(0.0); // Solde initial � z�ro

			// Insertion du compte
			serviceCompte.inserer(dto);

			// Message de succ�s
			modelInfo.setTitre("Inscription réussie");
			modelInfo.setTexte("Votre compte a été crée avec succès. Vous pouvez maintenant vous connecter.");
			return "connexion?faces-redirect=true";

		} catch (ExceptionValidation e) {
			UtilJsf.messageError("Erreur lors de l'inscription : " + e.getMessage());
			return null;
		} catch (Exception e) {
			UtilJsf.messageError("Une erreur inattendue s'est produite : " + e.getMessage());
			return null;
		}
	}
}
