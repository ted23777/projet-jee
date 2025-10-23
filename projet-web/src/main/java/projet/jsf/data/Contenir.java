package projet.jsf.data;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class Contenir implements Serializable {

    //-------
    // Champs
    //-------

	private int idContenir;
	
    private Integer idparcelle;

    private Integer idculture;

    private Double part;
    
    public Contenir() {
    	
    }

    public Contenir(Integer idparcelle, Integer idculture, Double part) {
		this.idparcelle = idparcelle;
		this.idculture = idculture;
		this.part = part;
	}

	//-------
    // Getters & Setters
    //-------

    public Integer getIdParcelle() {
        return idparcelle;
    }

    public void setIdParcelle(Integer idparcelle) {
        this.idparcelle = idparcelle;
    }

    public Integer getIdCulture() {
        return idculture;
    }

    public void setIdCulture(Integer idculture) {
        this.idculture = idculture;
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
		return Objects.hash(idculture, idContenir, idparcelle, part);
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
		return Objects.equals(idculture, other.idculture) && idContenir == other.idContenir
				&& Objects.equals(idparcelle, other.idparcelle) && Objects.equals(part, other.part);
	}
}
