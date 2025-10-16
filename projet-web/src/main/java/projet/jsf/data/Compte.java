package projet.jsf.data;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Compte implements Serializable  {

	//-------
	// Champs
	//-------
	
	private Integer		id;
	
	@NotBlank( message = "Le nom doit être renseigné")
	@Size(max=25, message = "Valeur trop longue pour le nom : 25 car. maxi" )
	private String		nom;

	@NotBlank( message = "Le prenom doit être renseigné")
	@Size(max=25, message = "Valeur trop longue pour le prenom : 25 car. maxi" )
	private String		prenom;
	
	@NotBlank( message = "Le mot de passe doit être renseigné")
	@Size(max=25, message = "Valeur trop longue pour le mot de passe : 25 car. maxi" )
	private String		motDePasse;

	@NotBlank( message = "L'adresse e-mail doit être renseigné")
	@Size(max=100, message = "Valeur trop longue pour l'adresse e-mail : 100 car. maxi" )
	@Email( message = "Adresse e-mail invalide" )
	private String		adresseMail;
	
	@NotBlank( message = "Le solde doit être renseigné")
	@Size(max=25, message = "Valeur trop longue pour le solde : 25 car. maxi" )
	private Double solde;
	
	private boolean	admin;
	

	//-------
	// Getters & setters
	//-------

	public Integer getId() {
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


	//-------
	// hashCode() & equals()
	//-------

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		var other = (Compte) obj;
		return Objects.equals(id, other.id);
	}
	
}
