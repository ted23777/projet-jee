package projet.ejb.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.IdClass;

@Entity
@Table(name = "concerner")
@IdClass(Concerner.class)  // Spécifie que la classe Concerner elle-même gère la clé primaire composite
public class Concerner implements Serializable {

    //-------
    // Champs
    //-------

    @Id
    @ManyToOne
    @JoinColumn(name = "idculture")
    private Culture culture;

    @Id
    @ManyToOne
    @JoinColumn(name = "identretien")
    private Entretien entretien;

    //-------
    // Getters & Setters
    //-------

    public Culture getCulture() {
        return culture;
    }

    public void setCulture(Culture culture) {
        this.culture = culture;
    }

    public Entretien getEntretien() {
        return entretien;
    }

    public void setEntretien(Entretien entretien) {
        this.entretien = entretien;
    }

    // Ajout des méthodes pour obtenir les IDs directement
    public Integer getIdCulture() {
        return culture != null ? culture.getId() : null;
    }

    public Integer getIdEntretien() {
        return entretien != null ? entretien.getId() : null;
    }

    //-------
    // equals() et hashCode()
    //-------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((culture == null) ? 0 : culture.hashCode());
        result = prime * result + ((entretien == null) ? 0 : entretien.hashCode());
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
        Concerner other = (Concerner) obj;
        if (culture == null) {
            if (other.culture != null)
                return false;
        } else if (!culture.equals(other.culture))
            return false;
        if (entretien == null) {
            if (other.entretien != null)
                return false;
        } else if (!entretien.equals(other.entretien))
            return false;
        return true;
    }
}
