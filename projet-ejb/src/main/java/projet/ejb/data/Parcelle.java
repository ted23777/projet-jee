package projet.ejb.data;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "parcelle")
public class Parcelle {

	// -------
	// Champs
	// -------

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idparcelle")
	private int id;

	@Column(name = "surface")
	private Double surface;

	@Column(name = "libre")
	private Boolean libre;

	@ManyToOne
	@JoinColumn(name = "idcompte")
	private Compte compte;

	// Relation correcte : Une parcelle peut contenir plusieurs "Contenir"
	@OneToMany(mappedBy = "parcelle", cascade = CascadeType.ALL)
	private List<Contenir> contenirs = new ArrayList<>();

	// -------
	// Constructeurs
	// -------

	public Parcelle() {
	}

	public Parcelle(Double surface, Boolean libre) {
		this.surface = surface;
		this.libre = libre;
	}

	// -------
	// Getters & Setters
	// -------

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getSurface() {
		return surface;
	}

	public void setSurface(Double surface) {
		this.surface = surface;
	}

	public Boolean getLibre() {
		return libre;
	}

	public void setLibre(Boolean libre) {
		this.libre = libre;
	}

	public Compte getCompte() {
		return compte;
	}

	public void setCompte(Compte compte) {
		this.compte = compte;
	}

	public List<Contenir> getContenirs() {
		return contenirs;
	}

	public void setContenirs(List<Contenir> contenirs) {
		this.contenirs = contenirs;
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
		Parcelle other = (Parcelle) obj;
		return id == other.id;
	}
}
