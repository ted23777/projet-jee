package projet.ejb.data;

import java.io.Serializable;

public class ContenirPK implements Serializable {
	
private static final long serialVersionUID = 1L;
    
    private int idParcelle;
    private int idCulture;
    
    //-------
    // Constructeurs
    //-------
    public ContenirPK() {
    }
    
    public ContenirPK(int idParcelle, int idCulture) {
        this.idParcelle = idParcelle;
        this.idCulture = idCulture;
    }
    
    //-------
    // Getters & Setters
    //-------
    public int getIdParcelle() {
        return idParcelle;
    }
    
    public void setIdParcelle(int idParcelle) {
        this.idParcelle = idParcelle;
    }
    
    public int getIdCulture() {
        return idCulture;
    }
    
    public void setIdCulture(int idCulture) {
        this.idCulture = idCulture;
    }
    
    //-------
    // equals() et hashcode()
    //-------
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idParcelle;
        result = prime * result + idCulture;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContenirPK other = (ContenirPK) obj;
        if (idParcelle != other.idParcelle)
            return false;
        if (idCulture != other.idCulture)
            return false;
        return true;
    }

}
