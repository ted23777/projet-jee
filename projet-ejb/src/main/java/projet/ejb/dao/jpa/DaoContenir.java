package projet.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import projet.ejb.dao.IDaoContenir;
import projet.ejb.data.Contenir;
import projet.ejb.data.Culture;
import projet.ejb.data.Parcelle;

@Stateless
@Local
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DaoContenir implements IDaoContenir {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void inserer(Contenir contenir) {
        em.persist(contenir);
        em.flush();
    }

    @Override
    public void modifier(Contenir contenir) {
        em.merge(contenir);
    }

    @Override
    public void supprimer(int idContenir) {
        em.remove(retrouver(idContenir));
    }

    @Override
    public Contenir retrouver(int idContenir) {
        return em.find(Contenir.class, idContenir);
    }

    @Override
    public List<Contenir> listerTout() {
        var jpql = "SELECT c FROM Contenir c ORDER BY c.parcelle.id";
        return em.createQuery(jpql, Contenir.class).getResultList();
    }

    @Override
    public List<Contenir> listerParParcelle(int idParcelle) {
        var jpql = "SELECT c FROM Contenir c WHERE c.parcelle.id = :idParcelle";
        var query = em.createQuery(jpql, Contenir.class);
        query.setParameter("idParcelle", idParcelle);
        return query.getResultList();
    }

    @Override
    public List<Contenir> listerParCulture(int idCulture) {
        var jpql = "SELECT c FROM Contenir c WHERE c.culture.id = :idCulture";
        var query = em.createQuery(jpql, Contenir.class);
        query.setParameter("idCulture", idCulture);
        return query.getResultList();
    }

    @Override
    public long compter() {
        var jpql = "SELECT COUNT(c) FROM Contenir c";
        return em.createQuery(jpql, Long.class).getSingleResult();
    }

    @Override
    public void supprimerParCulture(int idCulture) {
        var jpql = "DELETE FROM Contenir c WHERE c.culture.id = :idCulture";
        var query = em.createQuery(jpql);
        query.setParameter("idCulture", idCulture);
        query.executeUpdate();
    }

    @Override
    public void supprimerParParcelle(int idParcelle) {
        var jpql = "DELETE FROM Contenir c WHERE c.parcelle.id = :idParcelle";
        var query = em.createQuery(jpql);
        query.setParameter("idParcelle", idParcelle);
        query.executeUpdate();
    }

    @Override
    public double calculerPartTotaleParParcelle(int idParcelle) {
        var jpql = "SELECT COALESCE(SUM(c.part), 0) FROM Contenir c WHERE c.parcelle.id = :idParcelle";
        var query = em.createQuery(jpql, Double.class);
        query.setParameter("idParcelle", idParcelle);
        return query.getSingleResult();
    }

    @Override
    public void ajouterCultureAParcelle(int idParcelle, int idCulture, double part) {
        Parcelle parcelle = em.find(Parcelle.class, idParcelle);
        Culture culture = em.find(Culture.class, idCulture);

        if (parcelle != null && culture != null) {
            Contenir contenir = new Contenir(parcelle, culture, part);
            em.persist(contenir);
            em.flush();
        }
    }

    @Override
    public void retirerCultureDeParcelle(int idParcelle, int idCulture) {
        var jpql = "DELETE FROM Contenir c WHERE c.parcelle.id = :idParcelle AND c.culture.id = :idCulture";
        var query = em.createQuery(jpql);
        query.setParameter("idParcelle", idParcelle);
        query.setParameter("idCulture", idCulture);
        query.executeUpdate();
    }

    @Override
    public void modifierPartCulture(int idParcelle, int idCulture, double nouvellePart) {
        var jpql = "UPDATE Contenir c SET c.part = :nouvellePart WHERE c.parcelle.id = :idParcelle AND c.culture.id = :idCulture";
        var query = em.createQuery(jpql);
        query.setParameter("nouvellePart", nouvellePart);
        query.setParameter("idParcelle", idParcelle);
        query.setParameter("idCulture", idCulture);
        query.executeUpdate();
    }

    @Override
    public List<Culture> listerCulturesDeParcelle(int idParcelle) {
        var jpql = "SELECT c.culture FROM Contenir c WHERE c.parcelle.id = :idParcelle";
        var query = em.createQuery(jpql, Culture.class);
        query.setParameter("idParcelle", idParcelle);
        return query.getResultList();
    }

    @Override
    public List<Parcelle> listerParcellesAvecCulture(int idCulture) {
        var jpql = "SELECT c.parcelle FROM Contenir c WHERE c.culture.id = :idCulture";
        var query = em.createQuery(jpql, Parcelle.class);
        query.setParameter("idCulture", idCulture);
        return query.getResultList();
    }
}
