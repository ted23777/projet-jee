package projet.jsf.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Entretien implements Serializable {

    //-------
    // Champs
    //-------

    private Integer id;

    @NotNull(message = "La date de l'entretien doit être renseignée")
    private LocalDate date;

    @NotNull(message = "Le titre de l'entretien doit être renseigné")
    @Size(max = 50, message = "Le titre de l'entretien est trop long (maximum 50 caractères)")
    private String titre;

    //-------
    // Getters & Setters
    //-------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        var other = (Entretien) obj;
        return Objects.equals(id, other.id);
    }
}
