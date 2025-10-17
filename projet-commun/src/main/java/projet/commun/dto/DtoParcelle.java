package projet.commun.dto;

import java.io.Serializable;


@SuppressWarnings("serial")
public class DtoParcelle implements Serializable  {

	//-------
	// Champs
	//-------
	
	private Integer id;
    private Double surface;
    private Boolean libre;
    private Integer idCompte;

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public Boolean getLibre() {
        return libre;
    }

    public void setLibre(Boolean libre) {
        this.libre = libre;
    }

    public Integer getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Integer idCompte) {
        this.idCompte = idCompte;
    }
}
