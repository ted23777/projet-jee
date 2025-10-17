package projet.commun.service;

import java.util.List;

import projet.commun.dto.DtoEntretien;
import projet.commun.exception.ExceptionValidation;

public interface IServiceEntretien {

	int inserer(DtoEntretien entretien) throws ExceptionValidation;

	void modifier(DtoEntretien entretien) throws ExceptionValidation;

	void supprimer(int idEntretien) throws ExceptionValidation;

	DtoEntretien retrouver(int idEntretien);

	List<DtoEntretien> listerTout();

	List<DtoEntretien> rechercherParTitre(String titre);

	long compter();
}
