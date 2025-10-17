package projet.commun.dto;

import java.io.Serializable;


@SuppressWarnings("serial")
public class DtoContenir implements Serializable  {

	//-------
	// Champs
	//-------
	
	private Integer idParcelle;
    private Integer idCulture;
    private Double part;

    // Getters et Setters

    public Integer getIdParcelle() {
        return idParcelle;
    }

    public void setIdParcelle(Integer idParcelle) {
        this.idParcelle = idParcelle;
    }

    public Integer getIdCulture() {
        return idCulture;
    }

    public void setIdCulture(Integer idCulture) {
        this.idCulture = idCulture;
    }

    public Double getPart() {
        return part;
    }

    public void setPart(Double part) {
        this.part = part;
    }
}
