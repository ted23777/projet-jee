package projet.ejb.dao;

import java.util.List;

import projet.ejb.data.Compte;
import projet.ejb.data.Parcelle;


public interface IDaoCompte {

	int			inserer( Compte compte );

	void 		modifier( Compte compte );

	void 		supprimer( int idCompte );

	Compte 		retrouver( int idCompte );

	List<Compte> listerTout();

	Compte 		validerAuthentification( String email, String motDePasse );

	boolean verifierUniciteMail(String email, int idCompte);
	
	// 🔹 Côté utilisateur : lister ses propres parcelles
	List<Parcelle> listerParcellesDuCompte(int idCompte);

	// 🔹 Côté admin : lister tous les comptes ayant au moins une parcelle réservée
	List<Compte> listerComptesAvecParcelles();

}
