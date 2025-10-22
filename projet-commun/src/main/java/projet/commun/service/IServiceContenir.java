package projet.commun.service;

import java.util.List;

import projet.commun.dto.DtoContenir;
import projet.commun.dto.DtoCulture;
import projet.commun.dto.DtoParcelle;
import projet.commun.exception.ExceptionValidation;

public interface IServiceContenir {

    void inserer(DtoContenir contenir) throws ExceptionValidation;
    
    void modifier(DtoContenir contenir) throws ExceptionValidation;
    
    void supprimer(int idContenir) throws ExceptionValidation;
    
    DtoContenir retrouver(int idContenir);
    
    List<DtoContenir> listerTout();
    
    List<DtoContenir> listerParParcelle(int idParcelle);
    
    List<DtoContenir> listerParCulture(int idCulture);
    
    long compter();

 // --- Calculs spécifiques ---
    double getSommePartsParcelle(int idParcelle);

    Double getPartRestante(int idParcelle) ;

    // --- Cas d'utilisation métier ---

    // Ajouter une culture à une parcelle donnée
    void ajouterCultureAParcelle(int idParcelle, int idCulture, double part) throws ExceptionValidation;

    // Supprimer une culture d’une parcelle
    void retirerCultureDeParcelle(int idParcelle, int idCulture);

    // Modifier la part occupée par une culture
    void modifierPartCulture(int idParcelle, int idCulture, double nouvellePart);

    // Lister les cultures présentes sur une parcelle
    List<DtoCulture> listerCulturesDeParcelle(int idParcelle);

    // Lister les parcelles contenant une culture donnée
    List<DtoParcelle> listerParcellesAvecCulture(int idCulture);
    
}
