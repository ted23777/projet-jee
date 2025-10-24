package projet.ejb.data;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mouvement")
@NamedQueries({
		@NamedQuery(name = "Mouvement.findAll", query = "SELECT m FROM Mouvement m ORDER BY m.date DESC, m.id DESC"),
		@NamedQuery(name = "Mouvement.findByLibelle", query = "SELECT m FROM Mouvement m WHERE LOWER(m.libelle) LIKE :pattern ORDER BY m.date DESC"),
		@NamedQuery(name = "Mouvement.findByIdCompte", query = "SELECT m FROM Mouvement m WHERE m.compte.id = :idCompte ORDER BY m.date DESC"),
		@NamedQuery(name = "Mouvement.count", query = "SELECT COUNT(m) FROM Mouvement m") })
public class Mouvement {

	// -------
	// Champs
	// -------

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idmouvement")
	private int id;

	@Column(name = "date_", nullable = false)
	private LocalDate date;

	@Column(name = "libelle", nullable = false, length = 50)
	private String libelle;

	@Column(name = "montant", nullable = false, precision = 15, scale = 2)
	private BigDecimal montant;

	@ManyToOne
	@JoinColumn(name = "idcompte", nullable = false)
	private Compte compte;

	// -------
	// Getters & Setters
	// -------

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

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public Compte getCompte() {
		return compte;
	}

	public void setCompte(Compte compte) {
		this.compte = compte;
	}

	// -------
	// equals() et hashCode()
	// -------

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
		Mouvement other = (Mouvement) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
