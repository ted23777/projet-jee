package projet.commun.service;

import java.util.List;

import projet.commun.dto.DtoContenir;
import projet.commun.exception.ExceptionValidation;

public interface IServiceContenir {

    void inserer(DtoContenir contenir) throws ExceptionValidation;
    
    void modifier(DtoContenir contenir) throws ExceptionValidation;
    
    void supprimer(int idParcelle, int idCulture) throws ExceptionValidation;
    
    DtoContenir retrouver(int idParcelle, int idCulture);
    
    List<DtoContenir> listerTout();
    
    List<DtoContenir> listerParParcelle(int idParcelle);
    
    List<DtoContenir> listerParCulture(int idCulture);
    
    long compter();

	Double getPartRestante(int idParcelle);

	Double getSommePartsParcelle(int idParcelle);
    
}
