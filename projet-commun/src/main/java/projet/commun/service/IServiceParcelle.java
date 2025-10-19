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
}
