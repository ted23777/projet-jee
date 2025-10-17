package projet.ejb.dao;

import java.util.List;

import projet.ejb.data.Contenir;

public interface IDaoContenir {

    void inserer(Contenir contenir);

    void modifier(Contenir contenir);

    void supprimer(int idParcelle, int idCulture);

    Contenir retrouver(int idParcelle, int idCulture);

    List<Contenir> listerTout();
}
