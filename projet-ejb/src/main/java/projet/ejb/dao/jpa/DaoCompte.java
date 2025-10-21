package projet.ejb.dao.jpa;

import static javax.ejb.TransactionAttributeType.MANDATORY;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import projet.ejb.dao.IDaoCompte;
import projet.ejb.data.Compte;
import projet.ejb.data.Parcelle;


@Stateless
@Local
@TransactionAttribute(MANDATORY)
public class DaoCompte implements IDaoCompte {

    @PersistenceContext
    private EntityManager em;

    @Override
    public int inserer(Compte compte) {
        em.persist(compte);
        em.flush();
        return compte.getId();
    }

    @Override
    public void modifier(Compte compte) {
        em.merge(compte);
    }

    @Override
    public void supprimer(int idCompte) {
        em.remove(retrouver(idCompte));
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public Compte retrouver(int idCompte) {
        return em.find(Compte.class, idCompte);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<Compte> listerTout() {
        var jpql = "SELECT c FROM Compte c ORDER BY c.nom";
        return em.createQuery(jpql, Compte.class).getResultList();
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public Compte validerAuthentification(String email, String motDePasse) {
        var jpql = "SELECT c FROM Compte c WHERE c.adresseMail = :email AND c.motDePasse = :motDePasse";
        var query = em.createQuery(jpql, Compte.class);
        query.setParameter("email", email);
        query.setParameter("motDePasse", motDePasse);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public boolean verifierUniciteMail(String email, int idCompte) {
        var jpql = "SELECT COUNT(c) FROM Compte c WHERE c.adresseMail = :email AND c.id <> :idCompte";
        var query = em.createQuery(jpql, Long.class);
        query.setParameter("email", email);
        query.setParameter("idCompte", idCompte);
        return query.getSingleResult() == 0;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<Parcelle> listerParcellesDuCompte(int idCompte) {
        var jpql = "SELECT p FROM Parcelle p WHERE p.compte.id = :idCompte ORDER BY p.id";
        var query = em.createQuery(jpql, Parcelle.class);
        query.setParameter("idCompte", idCompte);
        return query.getResultList();
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<Compte> listerComptesAvecParcelles() {
        var jpql = "SELECT DISTINCT c FROM Compte c JOIN c.parcelles p ORDER BY c.nom";
        return em.createQuery(jpql, Compte.class).getResultList();
    }

    // Optionnel
    @TransactionAttribute(NOT_SUPPORTED)
    public Compte retrouverParEmail(String email) {
        var jpql = "SELECT c FROM Compte c WHERE c.adresseMail = :email";
        var query = em.createQuery(jpql, Compte.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

