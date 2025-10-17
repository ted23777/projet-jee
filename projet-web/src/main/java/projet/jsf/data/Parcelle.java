package projet.jsf.data;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
public class Parcelle implements Serializable {

    //-------
    // Champs
    //-------

    private Integer id;

    @NotNull(message = "La surface doit être renseignée")
    private Double surface;

    private Boolean libre;

    private Integer idCompte;
    
    //private String nomPrenom;

    //-------
    // Getters & Setters
    //-------

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
    

    //-------
    // hashCode() & equals()
    //-------

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        var other = (Parcelle) obj;
        return Objects.equals(id, other.id);
    }
}
