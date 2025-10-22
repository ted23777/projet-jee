package projet.commun.dto;

import java.io.Serializable;


@SuppressWarnings("serial")
public class DtoCompte implements Serializable  {

	//-------
	// Champs
	//-------
	
	private int			id;
	
	private String		nom;
	
	private String		prenom;
	
	private String		motDePasse;
	
	private String		adresseMail;

	private boolean		admin;
	
	private Double 		solde;
	
	//Constructeurs
	public DtoCompte() {
	    
	}

	public DtoCompte(int id, String nom, String prenom, String email, String motDePasse, boolean admin, Double solde) {
	    this.id = id;
	    this.nom = nom;
	    this.prenom = prenom;
	    this.adresseMail = email;
	    this.motDePasse = motDePasse;
	    this.admin = admin;
	    this.solde = solde;
	}

	
	
	//-------
	// Getters & setters
	//-------

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Double getSolde() {
		return solde;
	}

	public void setSolde(Double solde) {
		this.solde = solde;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	public String getAdresseMail() {
		return adresseMail;
	}

	public void setAdresseMail(String adresseMail) {
		this.adresseMail = adresseMail;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
