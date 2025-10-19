package projet.ejb.data;

import java.io.Serializable;

public class ConcernerPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idCulture;
    private int idEntretien;

    //-------
    // Constructeurs
    //-------

    public ConcernerPK() {
    }

    public ConcernerPK(int idCulture, int idEntretien) {
        this.idCulture = idCulture;
        this.idEntretien = idEntretien;
    }

    //-------
    // Getters & Setters
    //-------

    public int getIdCulture() {
        return idCulture;
    }

    public void setIdCulture(int idCulture) {
        this.idCulture = idCulture;
    }

    public int getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(int idEntretien) {
        this.idEntretien = idEntretien;
    }

    //-------
    // equals() et hashcode()
    //-------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idCulture;
        result = prime * result + idEntretien;
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
        ConcernerPK other = (ConcernerPK) obj;
        if (idCulture != other.idCulture)
            return false;
        if (idEntretien != other.idEntretien)
            return false;
        return true;
    }
}