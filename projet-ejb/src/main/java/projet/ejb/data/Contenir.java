package projet.ejb.data;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "contenir")
public class Contenir implements Serializable {
	
	//@EmbeddedId
	//private ContenirPK contenirPK;
	
	@Id
	@GeneratedValue( strategy = IDENTITY)
	@Column( name = "idContenir")
	private int idContenir;

	@ManyToOne
	@JoinColumn(name = "idParcelle")
	private Parcelle parcelle;

	@ManyToOne
	@JoinColumn(name = "idCulture")
	private Culture culture;


	@Column(name = "part")
	private Double part;

	// Constructeurs
	public Contenir() {

	}

	// Getters / Setters

	public Contenir(Parcelle parcelle, Culture culture, Double part) {
		this.parcelle = parcelle;
		this.culture = culture;
		this.part = part;
	}
	
	public int getIdContenir() {
	    return idContenir;
	}

	public void setIdContenir(int idContenir) {
	    this.idContenir = idContenir;
	}

	public Double getPart() {
		return part;
	}

	public void setPart(Double part) {
		this.part = part;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(culture, idContenir, parcelle, part);
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
		return Objects.equals(culture, other.culture) && idContenir == other.idContenir
				&& Objects.equals(parcelle, other.parcelle) && Objects.equals(part, other.part);
	}
	
}
