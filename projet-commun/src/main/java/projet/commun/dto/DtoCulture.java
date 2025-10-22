package projet.commun.dto;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class DtoCulture implements Serializable {

	// -------
	// Champs
	// -------

	private Integer id;
	private String nom;

	public DtoCulture() {
		// Constructeur vide requis pour le mapping et la sérialisation
	}

	//instancies des cultures avec juste un nom
	public DtoCulture(String nom) {
		this.nom = nom;
	}

	public DtoCulture(Integer id, String nom) {
		this.id = id;
		this.nom = nom;
	}

	// Getters et Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	// -------
	// Méthodes utilitaires
	// -------

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof DtoCulture))
			return false;
		DtoCulture other = (DtoCulture) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return String.format("DtoCulture [id=%d, nom=%s]", id, nom);
	}
}
