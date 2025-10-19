package projet.ejb.dao;

import java.util.List;

import projet.ejb.data.Culture;

public interface IDaoCulture {

	int inserer(Culture culture);

    void modifier(Culture culture);

    void supprimer(int idCulture);

    Culture retrouver(int idCulture);

    List<Culture> listerTout();

    List<Culture> rechercherParNom(String nom);

    long compter();

    boolean nomExiste(String nom, int idCulture);
}
