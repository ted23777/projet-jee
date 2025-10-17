package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
        var jpql = "SELECT e FROM Entretien e ORDER BY e.date DESC, e.titre ASC";
        var query = em.createQuery(jpql, Entretien.class);
        return query.getResultList();
    }

    @Override
    public List<Entretien> rechercherParTitre(String titre) {
        String crit = (titre == null) ? "" : titre.trim().toLowerCase();
        if (crit.isEmpty()) {
            return listerTout();
        }
        TypedQuery<Entretien> query =
            em.createNamedQuery("Entretien.findByTitre", Entretien.class);
        query.setParameter("pattern", "%" + crit + "%");
        return query.getResultList();
    }

    @Override
    public long compter() {
        return em.createNamedQuery("Entretien.count", Long.class)
                 .getSingleResult();
    }

    @Override
    public boolean titreExiste(String titre, int idEntretien) {
        String crit = (titre == null) ? "" : titre.trim();

        Long count = em.createNamedQuery("Entretien.existsByTitre", Long.class)
                       .setParameter("titre", crit)
                       .setParameter("idEntretien", idEntretien)
                       .getSingleResult();

        return count != null && count > 0;
    }
}
