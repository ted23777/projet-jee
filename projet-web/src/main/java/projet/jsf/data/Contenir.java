package projet.jsf.data;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class Contenir implements Serializable {

    //-------
    // Champs
    //-------

    private Integer idParcelle;

    private Integer idCulture;

    private Double part;

    //-------
    // Getters & Setters
    //-------

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
    // hashCode() & equals()
    //-------

    @Override
    public int hashCode() {
        return Objects.hash(idParcelle, idCulture);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        var other = (Contenir) obj;
        return Objects.equals(idParcelle, other.idParcelle) &&
               Objects.equals(idCulture, other.idCulture);
    }
}
