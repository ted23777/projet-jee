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
	    
	    long compter();
	    
	 // 🔹 Côté utilisateur : réserver une parcelle libre
	    void reserverParCompte(int idParcelle, int idCompte);

	    // 🔹 Côté admin : libérer une parcelle (annuler la réservation)
	    void libererParcelle(int idParcelle);

	    // 🔹 Côté admin : marquer une parcelle comme occupée (quand un adhérent l’a prise)
	    void occuperParcelle(int idParcelle);

	    // 🔹 Côté admin/utilisateur : vérifier si une parcelle est libre
	    boolean estLibre(int idParcelle);

	    // 🔹 Côté admin : obtenir toutes les parcelles occupées
	    List<Parcelle> listerOccupees();
	    
	    boolean appartientACompte(int idParcelle, int idCompte);
	    
	    void changerStatut(int idParcelle, boolean libre);
=======
    List<Parcelle> listerTout();

	List<Parcelle> listerParIdCompte(int idCompte);
>>>>>>> master
}
