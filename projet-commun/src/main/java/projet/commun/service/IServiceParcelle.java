package projet.commun.service;

import java.util.List;


import projet.commun.dto.DtoParcelle;
import projet.commun.exception.ExceptionValidation;


public interface IServiceParcelle {

    int inserer(DtoParcelle Parcelle)throws ExceptionValidation;

    void modifier(DtoParcelle Parcelle)throws ExceptionValidation;

    void supprimer(int idParcelle)throws ExceptionValidation;

    DtoParcelle retrouver(int idParcelle);

    List<DtoParcelle> listerTout();
    
    List<DtoParcelle> listerParCompte(int idCompte);  
    
    List<DtoParcelle> listerLibres();
    
    List<DtoParcelle> listerOccupees();

    long compter();

    // --- Cas d'utilisation métier ---
    
    // Réserver une parcelle libre pour un adhérent
    void reserverParCompte(int idParcelle, int idCompte) throws ExceptionValidation;

    // Libérer une parcelle (annuler réservation)
    void libererParcelle(int idParcelle);

    // Marquer une parcelle comme occupée (admin)
    void occuperParcelle(int idParcelle);

    // Vérifie si une parcelle est libre
    boolean estLibre(int idParcelle);

    // Vérifie si une parcelle appartient à un compte donné
    boolean appartientACompte(int idParcelle, int idCompte);
}
