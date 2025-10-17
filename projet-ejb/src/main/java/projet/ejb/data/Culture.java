package projet.ejb.data;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "culture")
public class Culture {

    //-------
    // Champs
    //-------

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idculture")
    private int id;

    @Column(name = "nom")
    private String nom;

    //-------
    // Getters & Setters
    //-------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
        Culture other = (Culture) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
