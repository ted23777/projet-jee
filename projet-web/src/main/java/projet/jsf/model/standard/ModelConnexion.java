package projet.jsf.model.standard;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoCompte;
import projet.commun.service.IServiceConnexion;
import projet.jsf.data.Compte;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.CompteActif;
import projet.jsf.util.UtilJsf;

@RequestScoped
@Named
public class ModelConnexion {

	// -------
	// Champs
	// -------

	private Compte courant;

	@Inject
	private CompteActif compteActif;
	@Inject
	private ModelInfo modelInfo;
	@EJB
	private IServiceConnexion serviceConnexion;
	@Inject
	private IMapper mapper;

	// -------
	// Getters
	// -------

	public Compte getCourant() {
		if (courant == null) {
			courant = new Compte();
		}
		return courant;
	}

	// -------
	// Actons
	// -------

	public String connect() {

		DtoCompte dto = serviceConnexion.sessionUtilisateurOuvrir(courant.getAdresseMail(), courant.getMotDePasse());

		if (dto != null) {

			mapper.update(compteActif, mapper.map(dto));
			modelInfo.setTitre("Bienvenue");
			modelInfo.setTexte("Vous êtes connecté en tant que '" + compteActif.getNom() + "'.");
			return "info";

		} else {
			UtilJsf.messageError("email ou mot de passe invalide.");
			return null;
		}
	}
	
	public CompteActif getCompteActif() {
		return  compteActif;

	}
	
	
}
