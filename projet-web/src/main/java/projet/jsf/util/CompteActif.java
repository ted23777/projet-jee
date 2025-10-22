package projet.jsf.util;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import projet.jsf.data.Compte;

@SuppressWarnings("serial")
@SessionScoped
@Named
public class CompteActif extends Compte {
	
    // Vérifie si un utilisateur est connecté
	public boolean isLoggedIn() {
		return getAdresseMail() != null;
	}	
	
    // Vérifie si l'utilisateur connecté est admin
	public boolean isAdmin() {
		return (isLoggedIn() && super.isAdmin());
	}

    // Déconnexion
	public String disconnect() {
	    UtilJsf.sessionInvalidate();
        UtilJsf.messageInfo("Vous avez été déconnecté");
	    return "/connexion?faces-redirect=true";
	}

    // Méthode appelée après une connexion réussie
    public String redirectionApresConnexion() {
        if (isAdmin()) {
            // Redirige vers la page admin
            return "comptes?faces-redirect=true";
        } else {
            // Redirige vers la page home utilisateur
            return "home?faces-redirect=true";
        }
    }
}
