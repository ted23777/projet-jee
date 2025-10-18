package projet.commun.dto;

import java.io.Serializable;
import java.time.LocalDate;


@SuppressWarnings("serial")
public class DtoEntretien implements Serializable  {

	//-------
	// Champs
	//-------

	private Integer id;
    private LocalDate date;
    private String titre;

    // Getters et Setters

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
}
