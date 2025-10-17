package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import projet.ejb.dao.IDaoEntretien;
import projet.ejb.data.Entretien;

@Stateless
@Local
@TransactionAttribute
public class DaoEntretien implements IDaoEntretien {

    @PersistenceContext
    private EntityManager em;

    @Override
    public int inserer(Entretien entretien) {
        em.persist(entretien);
        em.flush();
        return entretien.getId();
    }

    @Override
    public void modifier(Entretien entretien) {
        em.merge(entretien);
    }

    @Override
    public void supprimer(int idEntretien) {
        em.remove(retrouver(idEntretien));
    }

    @Override
    public Entretien retrouver(int idEntretien) {
        return em.find(Entretien.class, idEntretien);
    }

    @Override
    public List<Entretien> listerTout() {
        var jpql = "SELECT e FROM Entretien e ORDER BY e.date";
        var query = em.createQuery(jpql, Entretien.class);
        return query.getResultList();
    }
}
