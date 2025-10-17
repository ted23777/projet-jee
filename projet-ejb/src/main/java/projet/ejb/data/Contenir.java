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
@Table(name = "contenir")
@IdClass(Contenir.class) // Cette classe gère la clé primaire composite
public class Contenir implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name = "idparcelle")
	private Parcelle parcelle;

	@Id
	@ManyToOne
	@JoinColumn(name = "idculture")
	private Culture culture;

	@Column(name = "part")
	private Double part;

	// Les getters pour accéder aux IDs directement
	public Integer getIdParcelle() {
		return parcelle != null ? parcelle.getId() : null;
	}

	public Integer getIdCulture() {
		return culture != null ? culture.getId() : null;
	}

	// -------
	// Getters & Setters
	// -------

	public Parcelle getParcelle() {
		return parcelle;
	}

	public void setParcelle(Parcelle parcelle) {
		this.parcelle = parcelle;
	}

	public Culture getCulture() {
		return culture;
	}

	public void setCulture(Culture culture) {
		this.culture = culture;
	}

	public Double getPart() {
		return part;
	}

	public void setPart(Double part) {
		this.part = part;
	}

	// -------
	// equals() et hashCode()
	// -------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parcelle == null) ? 0 : parcelle.hashCode());
		result = prime * result + ((culture == null) ? 0 : culture.hashCode());
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
		Contenir other = (Contenir) obj;
		if (parcelle == null) {
			if (other.parcelle != null)
				return false;
		} else if (!parcelle.equals(other.parcelle))
			return false;
		if (culture == null) {
			if (other.culture != null)
				return false;
		} else if (!culture.equals(other.culture))
			return false;
		return true;
	}
}
