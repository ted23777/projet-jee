package projet.commun.dto;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class DtoMouvement implements Serializable  {

	//-------
	// Champs
	//-------
	
	private int id;
    private Date date;
    private String libelle;
    private Double montant;
    private int idCompte;

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public int getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(int idCompte) {
        this.idCompte = idCompte;
    }
}
