package projet.ejb.dao;

import java.util.List;

import projet.ejb.data.Mouvement;

public interface IDaoMouvement {

    int inserer(Mouvement mouvement);

    void modifier(Mouvement mouvement);

    void supprimer(int idMouvement);

    Mouvement retrouver(int idMouvement);

    List<Mouvement> listerTout();
}
