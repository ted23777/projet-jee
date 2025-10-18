package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
        TypedQuery<Mouvement> query = em.createNamedQuery("Mouvement.findAll", Mouvement.class);
        return query.getResultList();
    }

    @Override
    public List<Mouvement> rechercherParLibelle(String libelle) {
        String crit = (libelle == null) ? "" : libelle.trim().toLowerCase();
        if (crit.isEmpty()) {
            return listerTout();
        }
        TypedQuery<Mouvement> query = em.createNamedQuery("Mouvement.findByLibelle", Mouvement.class);
        query.setParameter("pattern", "%" + crit + "%");
        return query.getResultList();
    }

    @Override
    public List<Mouvement> listerParIdCompte(int idCompte) {
        TypedQuery<Mouvement> query = em.createNamedQuery("Mouvement.findByIdCompte", Mouvement.class);
        query.setParameter("idCompte", idCompte);
        return query.getResultList();
    }

    @Override
    public long compter() {
        TypedQuery<Long> query = em.createNamedQuery("Mouvement.count", Long.class);
        return query.getSingleResult();
    }
}
