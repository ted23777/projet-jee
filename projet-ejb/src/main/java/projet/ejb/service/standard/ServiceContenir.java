package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoContenir;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceContenir;
import projet.ejb.dao.IDaoContenir;
import projet.ejb.dao.IDaoCulture;
import projet.ejb.dao.IDaoParcelle;
import projet.ejb.data.Contenir;
import projet.ejb.data.Culture;
import projet.ejb.data.Parcelle;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceContenir implements IServiceContenir {

    //======================================================
    // 🔹 Injection des dépendances
    //======================================================
    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoContenir daoContenir;

    @Inject
    private IDaoParcelle daoParcelle;

    @Inject
    private IDaoCulture daoCulture;

    //======================================================
    // 🔹 Opérations principales (CRUD)
    //======================================================

    @Override
    public void inserer(DtoContenir dtoContenir) throws ExceptionValidation {
        // --- Validation basique des champs ---
        valider(dtoContenir);

        // --- Vérifie que la parcelle existe ---
        Parcelle parcelle = daoParcelle.retrouver(dtoContenir.getIdParcelle());
        if (parcelle == null) {
            throw new ExceptionValidation("Parcelle introuvable avec l'id : " + dtoContenir.getIdParcelle());
        }

        // --- Vérifie que la culture existe ---
        Culture culture = daoCulture.retrouver(dtoContenir.getIdCulture());
        if (culture == null) {
            throw new ExceptionValidation("Culture introuvable avec l'id : " + dtoContenir.getIdCulture());
        }

        // --- Vérifie qu’il n’existe pas déjà une association entre les deux ---
        Contenir existant = daoContenir.retrouver(dtoContenir.getIdParcelle(), dtoContenir.getIdCulture());
        if (existant != null) {
            throw new ExceptionValidation("Cette culture est déjà associée à cette parcelle.");
        }

        // --- Vérifie la validité du pourcentage ---
        if (dtoContenir.getPart() == null || dtoContenir.getPart() <= 0 || dtoContenir.getPart() > 100) {
            throw new ExceptionValidation("La part doit être comprise entre 0 et 100%.");
        }

        // --- Calcule la somme des parts existantes ---
        Double sommeExistante = getSommePartsParcelle(dtoContenir.getIdParcelle());
        Double nouvelleSomme = sommeExistante + dtoContenir.getPart();

        if (nouvelleSomme > 100) {
            throw new ExceptionValidation(
                String.format("La somme des parts dépasse 100%% (%.2f%% existant + %.2f%% ajouté).",
                              sommeExistante, dtoContenir.getPart())
            );
        }

        // --- Insertion finale ---
        Contenir entity = mapper.map(dtoContenir);
        daoContenir.inserer(entity);
    }

    @Override
    public void modifier(DtoContenir dtoContenir) throws ExceptionValidation {
        valider(dtoContenir);

        Contenir contenir = daoContenir.retrouver(dtoContenir.getIdParcelle(), dtoContenir.getIdCulture());
        if (contenir == null) {
            throw new ExceptionValidation("Association parcelle-culture introuvable.");
        }

        if (dtoContenir.getPart() == null || dtoContenir.getPart() <= 0 || dtoContenir.getPart() > 100) {
            throw new ExceptionValidation("La part doit être comprise entre 0 et 100%.");
        }

        Double sommeAutres = getSommePartsParcelleSaufCulture(dtoContenir.getIdParcelle(), dtoContenir.getIdCulture());
        Double nouvelleSomme = sommeAutres + dtoContenir.getPart();

        if (nouvelleSomme > 100) {
            throw new ExceptionValidation(
                String.format("La somme des parts dépasse 100%% (%.2f%% existantes + %.2f%%).",
                              sommeAutres, dtoContenir.getPart())
            );
        }

        daoContenir.modifier(mapper.map(dtoContenir));
    }

    @Override
    public void supprimer(int idParcelle, int idCulture) throws ExceptionValidation {
        Contenir contenir = daoContenir.retrouver(idParcelle, idCulture);
        if (contenir == null) {
            throw new ExceptionValidation("Association parcelle-culture introuvable.");
        }
        daoContenir.supprimer(idParcelle, idCulture);
    }

    //======================================================
    // 🔹 Méthodes de consultation (lecture)
    //======================================================

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoContenir retrouver(int idParcelle, int idCulture) {
        Contenir contenir = daoContenir.retrouver(idParcelle, idCulture);
        return (contenir != null) ? mapper.map(contenir) : null;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoContenir> listerTout() {
        List<DtoContenir> liste = new ArrayList<>();
        for (Contenir c : daoContenir.listerTout()) {
            liste.add(mapper.map(c));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoContenir> listerParParcelle(int idParcelle) {
        List<DtoContenir> liste = new ArrayList<>();
        for (Contenir c : daoContenir.listerParParcelle(idParcelle)) {
            liste.add(mapper.map(c));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoContenir> listerParCulture(int idCulture) {
        List<DtoContenir> liste = new ArrayList<>();
        for (Contenir c : daoContenir.listerParCulture(idCulture)) {
            liste.add(mapper.map(c));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public long compter() {
        return daoContenir.compter();
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public Double getSommePartsParcelle(int idParcelle) {
        List<Contenir> contenirs = daoContenir.listerParParcelle(idParcelle);
        double somme = 0.0;
        for (Contenir c : contenirs) {
            if (c.getPart() != null) {
                somme += c.getPart();
            }
        }
        return somme ;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public Double getPartRestante(int idParcelle) {
        Double sommeActuelle = getSommePartsParcelle(idParcelle);
        return 100.0 - sommeActuelle;
    }

    //======================================================
    // 🔹 Méthodes utilitaires internes
    //======================================================

    private void valider(DtoContenir dto) throws ExceptionValidation {
        if (dto == null) {
            throw new ExceptionValidation("Les données sont obligatoires.");
        }
        if (dto.getIdParcelle() == null) {
            throw new ExceptionValidation("L'identifiant de la parcelle est obligatoire.");
        }
        if (dto.getIdCulture() == null) {
            throw new ExceptionValidation("L'identifiant de la culture est obligatoire.");
        }
    }

    private Double getSommePartsParcelleSaufCulture(int idParcelle, int idCultureExclue) {
        List<Contenir> contenirs = daoContenir.listerParParcelle(idParcelle);
        double somme = 0.0;
        for (Contenir c : contenirs) {
            if (c.getIdCulture() != idCultureExclue && c.getPart() != null) {
                somme += c.getPart();
            }
        }
        return somme;
    }
}
