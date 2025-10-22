package projet.jsf.data;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class Contenir implements Serializable {

    //-------
    // Champs
    //-------

	private int idContenir;
	
    private Integer parcelle;

    private Integer culture;

    private Double part;
    
    public Contenir() {
    	
    }

    public Contenir(Integer parcelle, Integer culture, Double part) {
		this.parcelle = parcelle;
		this.culture = culture;
		this.part = part;
	}

	//-------
    // Getters & Setters
    //-------

    public Integer getIdParcelle() {
        return parcelle;
    }

    public void setIdParcelle(Integer parcelle) {
        this.parcelle = parcelle;
    }

    public Integer getIdCulture() {
        return culture;
    }

    public void setIdCulture(Integer culture) {
        this.culture = culture;
    }

    public Double getPart() {
        return part;
    }

    public void setPart(Double part) {
        this.part = part;
    }
    
    public int getIdContenir() {
	    return idContenir;
	}

	public void setIdContenir(int idContenir) {
	    this.idContenir = idContenir;
	}

    //-------
    // hashCode() & equals()
    //-------

	@Override
	public int hashCode() {
		return Objects.hash(culture, idContenir, parcelle, part);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contenir other = (Contenir) obj;
		return Objects.equals(culture, other.culture) && idContenir == other.idContenir
				&& Objects.equals(parcelle, other.parcelle) && Objects.equals(part, other.part);
	}
}
