package projet.ejb.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "concerner")
@IdClass(ConcernerPK.class)
@NamedQueries({
    @NamedQuery(
        name = "Concerner.findAll",
        query = "SELECT c FROM Concerner c ORDER BY c.idEntretien, c.idCulture"
    ),
    @NamedQuery(
        name = "Concerner.findByIdEntretien",
        query = "SELECT c FROM Concerner c WHERE c.idEntretien = :idEntretien"
    ),
    @NamedQuery(
        name = "Concerner.findByIdCulture",
        query = "SELECT c FROM Concerner c WHERE c.idCulture = :idCulture"
    ),
    @NamedQuery(
        name = "Concerner.count",
        query = "SELECT COUNT(c) FROM Concerner c"
    )
})
public class Concerner implements Serializable {

    private static final long serialVersionUID = 1L;

    //-------
    // Champs
    //-------

    @Id
    @Column(name = "idculture")
    private int idCulture;

    @Id
    @Column(name = "identretien")
    private int idEntretien;

    @ManyToOne
    @JoinColumn(name = "idculture", insertable = false, updatable = false)
    private Culture culture;

    @ManyToOne
    @JoinColumn(name = "identretien", insertable = false, updatable = false)
    private Entretien entretien;

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
        Concerner other = (Concerner) obj;
        if (idCulture != other.idCulture)
            return false;
        if (idEntretien != other.idEntretien)
            return false;
        return true;
    }
}
