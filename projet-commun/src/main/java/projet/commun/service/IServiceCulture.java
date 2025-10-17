package projet.commun.service;

import java.util.List;

import projet.commun.dto.DtoCulture;
import projet.commun.exception.ExceptionValidation;

public interface IServiceCulture {

	int inserer(DtoCulture culture) throws ExceptionValidation;

	void modifier(DtoCulture culture) throws ExceptionValidation;

	void supprimer(int idCulture) throws ExceptionValidation;

	DtoCulture retrouver(int idCulture);

	List<DtoCulture> listerTout();

	List<DtoCulture> rechercherParNom(String nom);

	long compter();
}
