package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import projet.ejb.dao.IDaoContenir;
import projet.ejb.data.Contenir;

@Stateless
@Local
@TransactionAttribute
public class DaoContenir implements IDaoContenir {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void inserer(Contenir contenir) {
        // Insertion de l'objet Contenir sans retour d'identifiant car il utilise une clé composite
        em.persist(contenir);
        em.flush();
    }

    @Override
    public void modifier(Contenir contenir) {
        // Mise à jour de l'objet Contenir
        em.merge(contenir);
    }

    @Override
    public void supprimer(int idParcelle, int idCulture) {
        // Recherche et suppression de l'objet Contenir en utilisant les clés composites
        em.remove(retrouver(idParcelle, idCulture));
    }

    @Override
    public Contenir retrouver(int idParcelle, int idCulture) {
        // Recherche de l'objet Contenir en utilisant la clé composite
        return em.find(Contenir.class, new Object[] { idParcelle, idCulture });
    }

    @Override
    public List<Contenir> listerTout() {
        // Listing de tous les objets Contenir
        var jpql = "SELECT c FROM Contenir c ORDER BY c.idParcelle";
        var query = em.createQuery(jpql, Contenir.class);
        return query.getResultList();
    }

	@Override
	public List<Contenir> listerParParcelle(int idParcelle) {
		var jpql = "SELECT c FROM Contenir c WHERE c.idParcelle = :idParcelle";
		var query = em.createQuery(jpql, Contenir.class);
		query.setParameter("idParcelle", idParcelle);
		return query.getResultList();
	}

	@Override
	public List<Contenir> listerParCulture(int idCulture) {
		var jpql = "SELECT c FROM Contenir c WHERE c.idCulture = :idCulture";
		var query = em.createQuery(jpql, Contenir.class);
		query.setParameter("idCulture", idCulture);
		return query.getResultList();
	}

	@Override
	public long compter() {
		var jpql = "SELECT COUNT(c) FROM Contenir c";
		var query = em.createQuery(jpql, Long.class);
		return query.getSingleResult();
	}
}
