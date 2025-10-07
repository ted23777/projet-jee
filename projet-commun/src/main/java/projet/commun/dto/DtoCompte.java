package projet.commun.dto;

import java.io.Serializable;


@SuppressWarnings("serial")
public class DtoCompte implements Serializable  {

	//-------
	// Champs
	//-------
	
	private int			id;
	
	private String		pseudo;
	
	private String		motDePasse;
	
	private String		email;

	private boolean		flagAdmin;
	
	//-------
	// Getters & setters
	//-------

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isFlagAdmin() {
		return flagAdmin;
	}
	
	public void setFlagAdmin(boolean flagAdmin) {
		this.flagAdmin = flagAdmin;
	}
}
