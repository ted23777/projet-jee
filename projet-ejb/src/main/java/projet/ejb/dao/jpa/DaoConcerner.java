package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import projet.ejb.dao.IDaoConcerner;
import projet.ejb.data.Concerner;

@Stateless
@Local
@TransactionAttribute
public class DaoConcerner implements IDaoConcerner {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void inserer(Concerner concerner) {
        // Insertion de l'objet Concerner sans renvoyer un identifiant unique
        em.persist(concerner);
        em.flush();
    }

    @Override
    public void modifier(Concerner concerner) {
        // Mise à jour de l'objet Concerner
        em.merge(concerner);
    }

    @Override
    public void supprimer(int idCulture, int idEntretien) {
        // Utilisation des deux composants de la clé composite pour retrouver et supprimer l'objet
        Concerner concerner = retrouver(idCulture, idEntretien);
        if (concerner != null) {
            em.remove(concerner);
        }
    }

    @Override
    public Concerner retrouver(int idCulture, int idEntretien) {
        // Recherche de l'objet Concerner basé sur la clé composite
        var jpql = "SELECT c FROM Concerner c WHERE c.culture.id = :idCulture AND c.entretien.id = :idEntretien";
        var query = em.createQuery(jpql, Concerner.class);
        query.setParameter("idCulture", idCulture);
        query.setParameter("idEntretien", idEntretien);
        return query.getSingleResult();
    }

    @Override
    public List<Concerner> listerTout() {
        // Listing de tous les objets Concerner
        var jpql = "SELECT c FROM Concerner c";
        var query = em.createQuery(jpql, Concerner.class);
        return query.getResultList();
    }
}
