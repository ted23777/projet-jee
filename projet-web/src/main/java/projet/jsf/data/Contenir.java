package projet.jsf.data;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
public class Contenir implements Serializable {

    //-------
    // Champs
    //-------

	private Integer idContenir;
	
   
    @NotNull(message = "La parcelle doit être renseignée")
    private Integer idParcelle;  // ✅ CORRIGÉ
    
    @NotNull(message = "La culture doit être renseignée")
    private Integer idCulture;   // ✅ CORRIGÉ
    
    @NotNull(message = "La part doit être renseignée")
    private Double part;
    
    public Contenir() {
    	
    }

    public Contenir(Integer idparcelle, Integer idculture, Double part) {
		this.idParcelle = idparcelle;
		this.idCulture = idculture;
		this.part = part;
	}

	//-------
    // Getters & Setters
    //-------

    public Integer getIdParcelle() {
        return idParcelle;
    }

    public void setIdParcelle(Integer idparcelle) {
        this.idParcelle = idparcelle;
    }

    public Integer getIdCulture() {
        return idCulture;
    }

    public void setIdCulture(Integer idculture) {
        this.idCulture = idculture;
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
		return Objects.hash(idCulture, idContenir, idParcelle, part);
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
		return Objects.equals(idCulture, other.idCulture) && idContenir == other.idContenir
				&& Objects.equals(idParcelle, other.idParcelle) && Objects.equals(part, other.part);
	}
}
