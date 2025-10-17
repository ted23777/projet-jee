package projet.commun.dto;

import java.io.Serializable;


@SuppressWarnings("serial")
public class DtoConcerner implements Serializable  {

	//-------
	// Champs
	//-------
	
	private int idCulture;
    private int idEntretien;

    // Getters et Setters

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
}
