package projet.ejb.dao;

import java.util.List;

import projet.ejb.data.Contenir;
import projet.ejb.data.Culture;
import projet.ejb.data.Parcelle;

public interface IDaoContenir {

	 	void inserer(Contenir contenir);

	    void modifier(Contenir contenir);

	    void supprimer(int idContenir);

	    //Contenir retrouver(int idParcelle, int idCulture);

	    List<Contenir> listerTout();

	    List<Contenir> listerParParcelle(int idParcelle);

	    List<Contenir> listerParCulture(int idCulture);

	    long compter();

		Contenir retrouver(int idContenir);
		
		void supprimerParCulture(int idCulture);
		    
		void supprimerParParcelle(int idParcelle);
	    
		double calculerPartTotaleParParcelle(int idParcelle);   
		
		// 🔹 Côté utilisateur : ajouter une culture dans une parcelle
		void ajouterCultureAParcelle(int idParcelle, int idCulture, double part);

		// 🔹 Côté utilisateur : supprimer une culture d’une parcelle (remplacement ou erreur)
		void retirerCultureDeParcelle(int idParcelle, int idCulture);

		// 🔹 Côté utilisateur : mettre à jour la part occupée par une culture
		void modifierPartCulture(int idParcelle, int idCulture, double nouvellePart);

		// 🔹 Côté admin : lister toutes les cultures plantées dans une parcelle donnée
		List<Culture> listerCulturesDeParcelle(int idParcelle);

		// 🔹 Côté admin : lister toutes les parcelles où une culture donnée est plantée
		List<Parcelle> listerParcellesAvecCulture(int idCulture);

}
