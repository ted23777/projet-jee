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
        var query = em.createQuery(jpql, Parcelle.class);
        return query.getResultList();
    }
    
    @Override
    public List<Parcelle> listerLibres() {
    	var jpql =  "SELECT p FROM Parcelle p WHERE p.libre = true ORDER BY p.id";
    	var query = em.createQuery(jpql, Parcelle.class);
    	return query.getResultList();
    }

	@Override
	public List<Parcelle> listerParCompte(int idCompte) {
		var jpql = "SELECT p FROM Parcelle p WHERE p.compte.id = :idCompte ORDER BY p.id";
		var query = em.createQuery(jpql, Parcelle.class);
		query.setParameter("idCompte", idCompte);
		return query.getResultList();
	}
}
