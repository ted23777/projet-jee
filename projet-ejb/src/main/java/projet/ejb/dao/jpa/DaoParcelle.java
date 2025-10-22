package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import projet.ejb.dao.IDaoParcelle;
import projet.ejb.data.Compte;
import projet.ejb.data.Parcelle;

@Stateless
@Local
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DaoParcelle implements IDaoParcelle {

    @PersistenceContext
    private EntityManager em;

    @Override
    public int inserer(Parcelle parcelle) {
        em.persist(parcelle);
        em.flush();
        return parcelle.getId();
    }

    @Override
    public void modifier(Parcelle parcelle) {
        em.merge(parcelle);
    }

    @Override
    public void supprimer(int idParcelle) {
        em.remove(retrouver(idParcelle));
    }

    @Override
    public Parcelle retrouver(int idParcelle) {
        return em.find(Parcelle.class, idParcelle);
    }

    @Override
    public List<Parcelle> listerTout() {
        var jpql = "SELECT p FROM Parcelle p ORDER BY p.id";
        return em.createQuery(jpql, Parcelle.class).getResultList();
    }

    @Override
    public List<Parcelle> listerLibres() {
        var jpql = "SELECT p FROM Parcelle p WHERE p.libre = true ORDER BY p.id";
        return em.createQuery(jpql, Parcelle.class).getResultList();
    }

    @Override
    public List<Parcelle> listerParCompte(int idCompte) {
        var jpql = "SELECT p FROM Parcelle p WHERE p.compte.id = :idCompte ORDER BY p.id";
        var query = em.createQuery(jpql, Parcelle.class);
        query.setParameter("idCompte", idCompte);
        return query.getResultList();
    }

    @Override
    public long compter() {
        var jpql = "SELECT COUNT(p) FROM Parcelle p";
        return em.createQuery(jpql, Long.class).getSingleResult();
    }

    @Override
    public void reserverParCompte(int idParcelle, int idCompte) {
        var parcelle = em.find(Parcelle.class, idParcelle);
        if (parcelle != null && Boolean.TRUE.equals(parcelle.getLibre())) {
            var compte = em.getReference(Compte.class, idCompte);
            parcelle.setCompte(compte);
            parcelle.setLibre(false);
            em.merge(parcelle);
        }
    }

    @Override
    public void libererParcelle(int idParcelle) {
        var parcelle = em.find(Parcelle.class, idParcelle);
        if (parcelle != null) {
            parcelle.setCompte(null);
            parcelle.setLibre(true);
            em.merge(parcelle);
        }
    }

    @Override
    public void occuperParcelle(int idParcelle) {
        var parcelle = em.find(Parcelle.class, idParcelle);
        if (parcelle != null) {
            parcelle.setLibre(false);
            em.merge(parcelle);
        }
    }

    @Override
    public boolean estLibre(int idParcelle) {
        var jpql = "SELECT p.libre FROM Parcelle p WHERE p.id = :idParcelle";
        var query = em.createQuery(jpql, Boolean.class);
        query.setParameter("idParcelle", idParcelle);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public List<Parcelle> listerOccupees() {
        var jpql = "SELECT p FROM Parcelle p WHERE p.libre = false ORDER BY p.id";
        return em.createQuery(jpql, Parcelle.class).getResultList();
    }

    @Override
    public boolean appartientACompte(int idParcelle, int idCompte) {
        var jpql = "SELECT COUNT(p) FROM Parcelle p WHERE p.id = :idParcelle AND p.compte.id = :idCompte";
        var query = em.createQuery(jpql, Long.class);
        query.setParameter("idParcelle", idParcelle);
        query.setParameter("idCompte", idCompte);
        return query.getSingleResult() > 0;
    }

    @Override
    public void changerStatut(int idParcelle, boolean libre) {
        var parcelle = em.find(Parcelle.class, idParcelle);
        if (parcelle != null) {
            parcelle.setLibre(libre);
            em.merge(parcelle);
            }
        }

    
    @Override
    public List<Parcelle> listerParIdCompte(int idCompte) {
        var jpql = "SELECT p FROM Parcelle p WHERE p.compte.id = :idCompte ORDER BY p.id";
        var query = em.createQuery(jpql, Parcelle.class);
        query.setParameter("idCompte", idCompte);
        return query.getResultList();

    }
}
