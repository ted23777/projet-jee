package projet.jsf.data;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class Concerner implements Serializable {

    //-------
    // Champs
    //-------

    private Integer idCulture;

    private Integer idEntretien;

    //-------
    // Getters & Setters
    //-------

    public Integer getIdCulture() {
        return idCulture;
    }

    public void setIdCulture(Integer idCulture) {
        this.idCulture = idCulture;
    }

    public Integer getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(Integer idEntretien) {
        this.idEntretien = idEntretien;
    }

    //-------
    // hashCode() & equals()
    //-------

    @Override
    public int hashCode() {
        return Objects.hash(idCulture, idEntretien);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        var other = (Concerner) obj;
        return Objects.equals(idCulture, other.idCulture) &&
               Objects.equals(idEntretien, other.idEntretien);
    }
}
