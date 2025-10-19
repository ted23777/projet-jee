package projet.ejb.data;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "contenir")
@IdClass(ContenirPK.class)
@NamedQueries({
		@NamedQuery(name = "Contenir.findAll", query = "SELECT c FROM Contenir c ORDER BY c.idParcelle, c.idCulture"),
		@NamedQuery(name = "Contenir.findByIdParcelle", query = "SELECT c FROM Contenir c WHERE c.idParcelle = :idParcelle"),
		@NamedQuery(name = "Contenir.findByIdCulture", query = "SELECT c FROM Contenir c WHERE c.idCulture = :idCulture"),
		@NamedQuery(name = "Contenir.count", query = "SELECT COUNT(c) FROM Contenir c") })
public class Contenir implements Serializable {

	@Id
	@Column(name = "idParcelle")
	private int idParcelle;

	@Id
	@Column(name = "idCulture")
	private int idCulture;

	@Column(name = "part")
	private Double part;

	// Associations JPA (optionnelles, si bidirectionnelles)
	@ManyToOne
	@JoinColumn(name = "idParcelle", insertable = false, updatable = false)
	private Parcelle parcelle;

	@ManyToOne
	@JoinColumn(name = "idCulture", insertable = false, updatable = false)
	private Culture culture;

	// Constructeurs
	public Contenir() {
	}

	public Contenir(int idParcelle, int idCulture, double part) {
		this.idParcelle = idParcelle;
		this.idCulture = idCulture;
		this.part = part;
	}

	// Getters / Setters
	public int getIdParcelle() {
		return idParcelle;
	}

	public void setIdParcelle(int idParcelle) {
		this.idParcelle = idParcelle;
	}

	public int getIdCulture() {
		return idCulture;
	}

	public void setIdCulture(int idCulture) {
		this.idCulture = idCulture;
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
}
