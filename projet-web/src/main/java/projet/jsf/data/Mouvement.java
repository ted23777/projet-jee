package projet.jsf.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class Mouvement implements Serializable {

    //-------
    // Champs
    //-------

    private Integer id;

    @NotNull(message = "La date du mouvement doit être renseignée")
    private LocalDate date;

    @NotNull(message = "Le libellé du mouvement doit être renseigné")
    @Size(min = 1, max = 50, message = "Le libellé doit contenir entre 1 et 50 caractères")
    private String libelle;

    @NotNull(message = "Le montant du mouvement doit être renseigné")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    private BigDecimal montant;

    private Integer idCompte;

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

    public Integer getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Integer idCompte) {
        this.idCompte = idCompte;
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
        var other = (Mouvement) obj;
        return Objects.equals(id, other.id);
    }
}
