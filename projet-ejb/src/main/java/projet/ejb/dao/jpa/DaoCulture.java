package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import projet.ejb.dao.IDaoCulture;
import projet.ejb.data.Culture;

@Stateless
@Local
@TransactionAttribute
public class DaoCulture implements IDaoCulture {

    @PersistenceContext
    private EntityManager em;

    @Override
    public int inserer(Culture culture) {
        em.persist(culture);
        em.flush();
        return culture.getId();
    }

    @Override
    public void modifier(Culture culture) {
        em.merge(culture);
    }

    @Override
    public void supprimer(int idCulture) {
        em.remove(retrouver(idCulture));
    }

    @Override
    public Culture retrouver(int idCulture) {
        return em.find(Culture.class, idCulture);
    }

    @Override
    public List<Culture> listerTout() {
        var jpql = "SELECT c FROM Culture c ORDER BY c.nom";
        var query = em.createQuery(jpql, Culture.class);
        return query.getResultList();
    }
}
