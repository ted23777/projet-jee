package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import projet.ejb.dao.IDaoMouvement;
import projet.ejb.data.Mouvement;

@Stateless
@Local
@TransactionAttribute
public class DaoMouvement implements IDaoMouvement {

    @PersistenceContext
    private EntityManager em;

    @Override
    public int inserer(Mouvement mouvement) {
        em.persist(mouvement);
        em.flush();
        return mouvement.getId();
    }

    @Override
    public void modifier(Mouvement mouvement) {
        em.merge(mouvement);
    }

    @Override
    public void supprimer(int idMouvement) {
        em.remove(retrouver(idMouvement));
    }

    @Override
    public Mouvement retrouver(int idMouvement) {
        return em.find(Mouvement.class, idMouvement);
    }

    @Override
    public List<Mouvement> listerTout() {
        var jpql = "SELECT m FROM Mouvement m ORDER BY m.date";
        var query = em.createQuery(jpql, Mouvement.class);
        return query.getResultList();
    }
}
