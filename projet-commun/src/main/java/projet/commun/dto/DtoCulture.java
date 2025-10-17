package projet.commun.dto;

import java.io.Serializable;


@SuppressWarnings("serial")
public class DtoCulture implements Serializable  {

	//-------
	// Champs
	//-------
	
	private Integer id;
    private String nom;

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
