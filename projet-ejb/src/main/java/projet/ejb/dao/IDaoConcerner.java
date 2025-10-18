package projet.ejb.dao;

import java.util.List;

import projet.ejb.data.Concerner;

public interface IDaoConcerner {

    void inserer(Concerner concerner);

    void modifier(Concerner concerner);

    void supprimer(int idCulture, int idEntretien);

    Concerner retrouver(int idCulture, int idEntretien);

    List<Concerner> listerTout();

    List<Concerner> listerParIdEntretien(int idEntretien);

    List<Concerner> listerParIdCulture(int idCulture);

    long compter();
}
