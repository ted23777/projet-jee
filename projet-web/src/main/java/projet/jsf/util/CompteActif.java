package projet.jsf.util;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import projet.jsf.data.Compte;

@SuppressWarnings("serial")
@SessionScoped
@Named
public class CompteActif extends Compte {
	
	public boolean isLoggedIn() {
		return getAdresseMail() != null;
	}	
	
	public boolean isAdmin() {
		return (isLoggedIn() && super.isAdmin());
	}

	public String disconnect() {
	    UtilJsf.sessionInvalidate();
        UtilJsf.messageInfo( "Vous avez été déconnecté" );
	    return "connexion";
	}

}
