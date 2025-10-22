package projet.commun.service;

import java.util.List;


import projet.commun.dto.DtoCompte;
import projet.commun.dto.DtoParcelle;
import projet.commun.exception.ExceptionValidation;


public interface IServiceCompte {
	
	int				inserer( DtoCompte dtoCompte ) throws ExceptionValidation;

	void			modifier( DtoCompte dtoCompte ) throws ExceptionValidation; 

	void			supprimer( int idCompte ) throws ExceptionValidation;

	DtoCompte 		retrouver( int idCompte ) ;

	List<DtoCompte>	listerTout() ;
	
	 // Authentification utilisateur
    DtoCompte validerAuthentification(String email, String motDePasse);

    // Vérifie qu'une adresse mail n’est pas déjà utilisée
    boolean verifierUniciteMail(String email, int idCompte);

    // Liste des parcelles d’un compte (adhérent)
    List<DtoParcelle> listerParcellesDuCompte(int idCompte);

    // Liste des comptes ayant au moins une parcelle (admin)
    List<DtoCompte> listerComptesAvecParcelles();

}
