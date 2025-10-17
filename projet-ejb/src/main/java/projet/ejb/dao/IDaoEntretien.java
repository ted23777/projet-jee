package projet.ejb.dao;

import java.util.List;

import projet.ejb.data.Entretien;

public interface IDaoEntretien {

    int inserer(Entretien entretien);

    void modifier(Entretien entretien);

    void supprimer(int idEntretien);

    Entretien retrouver(int idEntretien);

    List<Entretien> listerTout();
}
