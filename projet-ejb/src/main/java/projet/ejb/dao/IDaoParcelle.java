package projet.ejb.dao;

import java.util.List;

import projet.ejb.data.Parcelle;

public interface IDaoParcelle {

	 	int inserer(Parcelle parcelle);

	    void modifier(Parcelle parcelle);

	    void supprimer(int idParcelle);

	    Parcelle retrouver(int idParcelle);

	    List<Parcelle> listerTout();

	    List<Parcelle> listerLibres();

	    List<Parcelle> listerParCompte(int idCompte);
}
