package projet.commun.dto;

import java.io.Serializable;
import java.util.Objects;


@SuppressWarnings("serial")
public class DtoContenir implements Serializable  {

	//-------
	// Champs
	//-------
	private Integer idContenir;

	private Integer idParcelle;
    private Integer idCulture;
    private Double part;

    public DtoContenir() {
    	
    }

    public DtoContenir(Integer idParcelle, Integer idCulture, Double part) {
		this.idParcelle = idParcelle;
		this.idCulture = idCulture;
		this.part = part;
	}

    // Getters et Setters
	public Integer getIdContenir() {
		return idContenir;
	}

	public void setIdContenir(Integer idContenir) {
		this.idContenir = idContenir;
	}

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
    //-------
    // Méthodes utilitaires
    //-------

    @Override
    public int hashCode() {
        return Objects.hash(idParcelle, idCulture);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DtoContenir)) return false;
        DtoContenir other = (DtoContenir) obj;
        return Objects.equals(idParcelle, other.idParcelle)
            && Objects.equals(idCulture, other.idCulture);
    }

    @Override
    public String toString() {
        return String.format("DtoContenir [idParcelle=%d, idCulture=%d, part=%.2f%%]",
                idParcelle, idCulture, part);
    }
}
