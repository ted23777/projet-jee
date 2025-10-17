package projet.commun.service;

import java.util.List;

import projet.commun.dto.DtoConcerner;
import projet.commun.exception.ExceptionValidation;

public interface IServiceConcerner {

	void inserer(DtoConcerner concerner) throws ExceptionValidation;

	void modifier(DtoConcerner concerner) throws ExceptionValidation;

	void supprimer(int idCulture, int idEntretien) throws ExceptionValidation;

	DtoConcerner retrouver(int idCulture, int idEntretien);

	List<DtoConcerner> listerTout();

	List<DtoConcerner> listerParIdEntretien(int idEntretien);

	List<DtoConcerner> listerParIdCulture(int idCulture);

	long compter();
}
