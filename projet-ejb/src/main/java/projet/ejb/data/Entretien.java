package projet.ejb.data;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "entretien")
@NamedQueries({
    @NamedQuery(
        name = "Entretien.findAll",
        query = "SELECT e FROM Entretien e ORDER BY e.date DESC, e.titre ASC"
    ),
    @NamedQuery(
        name = "Entretien.findByTitre",
        query = "SELECT e FROM Entretien e " +
                "WHERE LOWER(e.titre) LIKE :pattern " +
                "ORDER BY e.date DESC, e.titre ASC"
    ),
    @NamedQuery(
        name = "Entretien.count",
        query = "SELECT COUNT(e) FROM Entretien e"
    ),
    @NamedQuery(
        name = "Entretien.existsByTitre",
        query = "SELECT COUNT(e) FROM Entretien e WHERE e.titre = :titre AND (:idEntretien = 0 OR e.id <> :idEntretien)"
    )
})
public class Entretien {

    //-------
    // Champs
    //-------

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "identretien")
    private int id;

    @Column(name = "date_")
    private LocalDate date;

    @Column(name = "titre")
    private String titre;

    //-------
    // Getters & Setters
    //-------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    //-------
    // equals() et hashcode()
    //-------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        Entretien other = (Entretien) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
