package projet.commun.dto;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class DtoParcelle implements Serializable  {

	//-------
	// Champs
	//-------
	
	private Integer id;
    private Double surface;
    private Boolean libre;
    private Integer idCompte;

    //-------
    // Constructeurs
    //-------

    public DtoParcelle() {
        // Constructeur vide requis pour le mapping et la sérialisation
    }

    public DtoParcelle(Integer id, Double surface, Boolean libre, Integer idCompte) {
        this.id = id;
        this.surface = surface;
        this.libre = libre;
        this.idCompte = idCompte;
    }
    
    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Integer idCompte) {
        this.idCompte = idCompte;
    }
    
  //-------
    // Méthodes utilitaires
    //-------

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DtoParcelle)) return false;
        DtoParcelle other = (DtoParcelle) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return String.format(
            "DtoParcelle [id=%d, surface=%.2f m², libre=%s, idCompte=%s]",
            id,
            (surface != null ? surface : 0.0), (libre != null ? libre : "inconnu"),  (idCompte != null ? idCompte : "aucun")
        );
    }
}
