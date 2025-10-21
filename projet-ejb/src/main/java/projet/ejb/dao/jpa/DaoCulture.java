package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import projet.ejb.dao.IDaoCulture;
import projet.ejb.data.Culture;

@Stateless
@Local
@TransactionAttribute(TransactionAttributeType.REQUIRED)
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
        var culture = retrouver(idCulture);
        if (culture != null) {
            em.remove(culture);
        }
    }

    @Override
    public Culture retrouver(int idCulture) {
        return em.find(Culture.class, idCulture);
    }

    @Override
    public List<Culture> listerTout() {
        var jpql = "SELECT c FROM Culture c ORDER BY c.nom";
        return em.createQuery(jpql, Culture.class).getResultList();
    }

    @Override
    public List<Culture> rechercherParNom(String nom) {
        var crit = (nom == null) ? "" : nom.trim().toLowerCase();
        if (crit.isEmpty()) {
            return listerTout();
        }
        var jpql = "SELECT c FROM Culture c WHERE LOWER(c.nom) LIKE :pattern ORDER BY c.nom ASC";
        var query = em.createQuery(jpql, Culture.class);
        query.setParameter("pattern", "%" + crit + "%");
        return query.getResultList();
    }

    @Override
    public long compter() {
        var jpql = "SELECT COUNT(c) FROM Culture c";
        return em.createQuery(jpql, Long.class).getSingleResult();
    }

    @Override
    public Culture retrouverParNom(String nom) {
        var jpql = "SELECT c FROM Culture c WHERE LOWER(c.nom) = :nom";
        var query = em.createQuery(jpql, Culture.class);
        query.setParameter("nom", nom.trim().toLowerCase());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
